package com.akash.gkgsmaster.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.Question
import com.akash.gkgsmaster.databinding.FragmentAdminAddQuestionBinding
import com.akash.gkgsmaster.ui.quiz.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class AdminAddQuestionFragment : Fragment(R.layout.fragment_admin_add_question) {

    private var _binding: FragmentAdminAddQuestionBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: QuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminAddQuestionBinding.bind(view)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.btnSave.setOnClickListener {
            val qText = binding.etQuestion.text.toString()
            val opt1 = binding.etOption1.text.toString()
            val opt2 = binding.etOption2.text.toString()
            val opt3 = binding.etOption3.text.toString()
            val opt4 = binding.etOption4.text.toString()
            val category = binding.etCategory.text.toString()
            val expl = binding.etExplanation.text.toString()

            if (qText.isBlank() || opt1.isBlank() || opt2.isBlank()) {
                Toast.makeText(context, "Question and at least 2 options are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val question = Question(
                id = UUID.randomUUID().toString(),
                text = qText,
                options = listOf(opt1, opt2, opt3, opt4).filter { it.isNotBlank() },
                correctOptionIndex = 0, // In this simple form, first option is correct
                category = category,
                explanation = expl
            )

            viewModel. insertQuestion(question)
            println("Admin: Added question ${question.text}")
            Toast.makeText(context, "Question Added Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
