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

    private lateinit var adapter: OptionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizBinding.bind(view)

        val category = arguments?.getString("category") ?: "General"
        val difficulty = arguments?.getString("difficulty") ?: "Medium"
        viewModel.startQuiz(category, difficulty)
        
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = OptionAdapter { index ->
            viewModel.submitAnswer(index)
        }
        binding.rvOptions.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.questionsState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show progress if no data yet
                    if (resource.data.isNullOrEmpty()) {
                        // binding.progressBar.visibility = View.VISIBLE
                    } else {
                        updateUI(resource.data, viewModel.currentQuestionIndex.value ?: 0)
                    }
                }
                is Resource.Success -> {
                    // binding.progressBar.visibility = View.GONE
                    resource.data?.let { updateUI(it, viewModel.currentQuestionIndex.value ?: 0) }
                }
                is Resource.Error -> {
                    // binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.currentQuestionIndex.observe(viewLifecycleOwner) { index ->
            viewModel.questionsState.value?.data?.let { updateUI(it, index) }
        }

        viewModel.timerText.observe(viewLifecycleOwner) { time ->
            binding.tvTimer.text = time
        }

        viewModel.isQuizFinished.observe(viewLifecycleOwner) { result ->
            result?.let {
                val bundle = Bundle().apply { putParcelable("result", it) }
                findNavController().navigate(R.id.action_quizFragment_to_quizResultFragment, bundle)
            }
        }
    }

    private fun updateUI(questions: List<com.akash.gkgsmaster.data.model.Question>, index: Int) {
        if (index >= questions.size) return
        
        val question = questions[index]
        binding.tvQuestion.text = question.text
        binding.tvQuestionCount.text = getString(R.string.question_count_placeholder, index + 1, questions.size)
        binding.quizProgress.progress = ((index + 1) * 100 / questions.size)
        adapter.setOptions(question.options)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
