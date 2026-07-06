package com.akash.gkgsmaster.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.Question
import com.akash.gkgsmaster.databinding.FragmentAdminQuizBinding
import com.akash.gkgsmaster.ui.quiz.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminQuizFragment : Fragment(R.layout.fragment_admin_quiz) {

    private var _binding: FragmentAdminQuizBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminQuizBinding.bind(view)

        setupClickListeners()
        
        viewModel.questionsState.observe(viewLifecycleOwner) {
            // Observe questions if needed
            println("Admin: Observed questions update")
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.fabAddQuestion.setOnClickListener {
            findNavController().navigate(R.id.action_adminQuizFragment_to_adminAddQuestionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
