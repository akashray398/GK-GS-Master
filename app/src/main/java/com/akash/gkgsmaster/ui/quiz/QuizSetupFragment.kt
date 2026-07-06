package com.akash.gkgsmaster.ui.quiz

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentQuizSetupBinding
import com.akash.gkgsmaster.ui.home.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizSetupFragment : Fragment(R.layout.fragment_quiz_setup) {

    private var _binding: FragmentQuizSetupBinding? = null
    private val binding get() = _binding!!

    private var selectedCategoryId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizSetupBinding.bind(view)

        binding.limitGroup.check(R.id.btn10q)
        binding.difficultyGroup.check(R.id.btnMedium)
        binding.languageGroup.check(R.id.btnEnglish)
        
        binding.tvTimerValue.text = getString(R.string.seconds_format, 30)
        binding.timerSlider.addOnChangeListener { _, value, _ ->
            binding.tvTimerValue.text = getString(R.string.seconds_format, value.toInt())
        }

        setupCategoryList()
        setupClickListeners()
    }

    private fun setupCategoryList() {
        val adapter = CategoryAdapter { category ->
            selectedCategoryId = category.id
        }
        binding.rvCategories.adapter = adapter
        
        // Populate with quiz categories
        adapter.submitList(listOf(
            com.akash.gkgsmaster.data.model.HomeCategory("IndiaHistory", "Indian History", android.R.drawable.ic_menu_today, R.color.primary),
            com.akash.gkgsmaster.data.model.HomeCategory("IndiaPolity", "Indian Polity", android.R.drawable.ic_menu_view, R.color.accent),
            com.akash.gkgsmaster.data.model.HomeCategory("IndiaGeo", "Indian Geography", android.R.drawable.ic_menu_mapmode, R.color.secondary),
            com.akash.gkgsmaster.data.model.HomeCategory("IndiaMyth", "Indian Mythology", android.R.drawable.star_on, R.color.glow),
            com.akash.gkgsmaster.data.model.HomeCategory("IndiaEco", "Indian Economy", android.R.drawable.ic_menu_save, R.color.success),
            com.akash.gkgsmaster.data.model.HomeCategory("ModernIndia", "Modern India", android.R.drawable.ic_menu_recent_history, R.color.primary),
            com.akash.gkgsmaster.data.model.HomeCategory("9", "General Knowledge", android.R.drawable.ic_menu_help, R.color.primary),
            com.akash.gkgsmaster.data.model.HomeCategory("17", "Science", android.R.drawable.ic_menu_compass, R.color.secondary),
            com.akash.gkgsmaster.data.model.HomeCategory("23", "World History", android.R.drawable.ic_menu_agenda, R.color.accent),
            com.akash.gkgsmaster.data.model.HomeCategory("24", "Politics", android.R.drawable.ic_menu_myplaces, R.color.primary),
            com.akash.gkgsmaster.data.model.HomeCategory("26", "Famous People", android.R.drawable.ic_menu_gallery, R.color.secondary)
        ))
    }

    private fun setupClickListeners() {
        binding.btnStartQuiz.setOnClickListener {
            if (selectedCategoryId == null) {
                Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val difficulty = when (binding.difficultyGroup.checkedButtonId) {
                R.id.btnEasy -> "easy"
                R.id.btnHard -> "hard"
                else -> "medium"
            }

            val amount = when (binding.limitGroup.checkedButtonId) {
                R.id.btn15q -> 15
                R.id.btn20q -> 20
                else -> 10
            }

            val timer = binding.timerSlider.value.toInt()
            val language = if (binding.languageGroup.checkedButtonId == R.id.btnHindi) "hindi" else "english"

            val bundle = Bundle().apply {
                putString("category", selectedCategoryId)
                putString("difficulty", difficulty)
                putInt("amount", amount)
                putInt("timer", timer)
                putString("language", language)
            }
            findNavController().navigate(R.id.action_quizSetupFragment_to_quizFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
