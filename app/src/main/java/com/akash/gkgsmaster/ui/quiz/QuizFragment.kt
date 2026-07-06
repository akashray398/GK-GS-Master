package com.akash.gkgsmaster.ui.quiz

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentQuizBinding
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by viewModels()
    private lateinit var optionsAdapter: OptionsAdapter

    private var selectedOptionIndex: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizBinding.bind(view)

        val category = arguments?.getString("category")
        val difficulty = arguments?.getString("difficulty")
        val timerOption = arguments?.getInt("timer") ?: 30
        val amount = arguments?.getInt("amount") ?: 10
        val isDailyChallenge = arguments?.getBoolean("isDailyChallenge") ?: false
        val language = arguments?.getString("language") ?: "english"

        viewModel.startQuiz(
            amount = amount,
            category = category,
            difficulty = difficulty,
            timerOption = timerOption,
            dailyChallenge = isDailyChallenge,
            language = language
        )

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        optionsAdapter = OptionsAdapter { index ->
            selectedOptionIndex = index
        }
        binding.rvOptions.adapter = optionsAdapter
    }

    private fun setupObservers() {
        viewModel.questionsState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.loadingSpinner.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.cvQuestion.visibility = View.GONE
                    binding.rvOptions.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.loadingSpinner.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    if (resource.data.isNullOrEmpty()) {
                        binding.tvError.text = "No questions found. Try a different category."
                        binding.tvError.visibility = View.VISIBLE
                    } else {
                        binding.cvQuestion.visibility = View.VISIBLE
                        binding.rvOptions.visibility = View.VISIBLE
                        binding.btnNext.visibility = View.VISIBLE
                        updateQuestionUI()
                    }
                }
                is Resource.Error -> {
                    binding.loadingSpinner.visibility = View.GONE
                    binding.tvError.text = resource.message
                    binding.tvError.visibility = View.VISIBLE
                    if (!resource.data.isNullOrEmpty()) {
                        binding.cvQuestion.visibility = View.VISIBLE
                        binding.rvOptions.visibility = View.VISIBLE
                        binding.btnNext.visibility = View.VISIBLE
                        updateQuestionUI()
                    }
                }
            }
        }

        viewModel.currentQuestionIndex.observe(viewLifecycleOwner) {
            updateQuestionUI()
        }

        viewModel.timerText.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        viewModel.timerProgress.observe(viewLifecycleOwner) { _ ->
            // Question timer progress
        }

        viewModel.isQuizFinished.observe(viewLifecycleOwner) { result ->
            result?.let {
                val action = QuizFragmentDirections.actionQuizFragmentToQuizResultFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun updateQuestionUI() {
        val questions = viewModel.questionsState.value?.data
        if (questions.isNullOrEmpty()) {
            println("QuizFragment: No questions available to display")
            return
        }
        
        val index = viewModel.currentQuestionIndex.value ?: 0
        if (index < questions.size) {
            val question = questions[index]
            println("QuizFragment: Displaying question ${index + 1}")
            binding.tvQuestion.text = question.text
            binding.tvQuestionCount.text = getString(R.string.question_count_format, index + 1, questions.size)
            binding.progressBar.progress = ((index + 1) * 100) / questions.size
            
            optionsAdapter.setOptions(question.options, question.correctOptionIndex)
            selectedOptionIndex = -1
        } else {
            println("QuizFragment: Index $index out of bounds for size ${questions.size}")
        }
    }

    private fun setupClickListeners() {
        binding.btnNext.setOnClickListener {
            if (selectedOptionIndex == -1) {
                Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.submitAnswer(selectedOptionIndex)
            viewModel.moveToNextQuestion()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
