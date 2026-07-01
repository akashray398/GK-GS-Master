package com.akash.gkgsmaster.ui.quiz

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.QuizCategory
import com.akash.gkgsmaster.databinding.FragmentQuizSetupBinding
import com.akash.gkgsmaster.ui.home.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizSetupFragment : Fragment(R.layout.fragment_quiz_setup) {

    private var _binding: FragmentQuizSetupBinding? = null
    private val binding get() = _binding!!
    private var selectedCategory: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizSetupBinding.bind(view)

        setupCategories()
        setupListeners()
    }

    private fun setupCategories() {
        val categories = listOf(
            QuizCategory("1", "Polity", R.drawable.ic_placeholder),
            QuizCategory("2", "History", R.drawable.ic_placeholder),
            QuizCategory("3", "Geography", R.drawable.ic_placeholder),
            QuizCategory("4", "Science", R.drawable.ic_placeholder)
        )
        
        // Using a similar adapter structure but mapped for QuizCategory
        val adapter = CategoryAdapter { category ->
            selectedCategory = category.title
        }
        // Need to map QuizCategory to HomeCategory for adapter reuse or create new adapter
        // For simplicity, let's assume we use HomeCategory or similar
    }

    private fun setupListeners() {
        binding.btnStartQuiz.setOnClickListener {
            val difficulty = when (binding.difficultyGroup.checkedButtonId) {
                R.id.btnEasy -> "Easy"
                R.id.btnMedium -> "Medium"
                R.id.btnHard -> "Hard"
                else -> "Medium"
            }
            
            selectedCategory = "General" // Forcing for now
            
            if (selectedCategory != null) {
                val bundle = Bundle().apply {
                    putString("category", selectedCategory)
                    putString("difficulty", difficulty)
                }
                findNavController().navigate(R.id.action_quizSetupFragment_to_quizFragment, bundle)
            } else {
                Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
