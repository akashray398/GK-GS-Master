package com.akash.gkgsmaster.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.QuizResult
import com.akash.gkgsmaster.databinding.FragmentQuizResultBinding
import dagger.hilt.android.AndroidEntryPoint

import android.os.Build
import java.util.Locale

@AndroidEntryPoint
class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {

    private var _binding: FragmentQuizResultBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizResultBinding.bind(view)

        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("result", QuizResult::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("result")
        }
        println("QuizResultFragment: score=${result?.correctAnswers}")
        result?.let { displayResult(it) }

        setupListeners()
    }

    private fun displayResult(result: QuizResult) {
        binding.tvScore.text = getString(R.string.score_placeholder, result.correctAnswers, result.totalQuestions)
        
        val minutes = result.timeTakenSeconds / 60
        val seconds = result.timeTakenSeconds % 60
        binding.tvTimeTaken.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        
        val accuracy = if (result.totalQuestions > 0) {
            (result.correctAnswers.toFloat() / result.totalQuestions * 100).toInt()
        } else 0
        binding.tvAccuracy.text = getString(R.string.accuracy_placeholder, accuracy)
        
        binding.tvXpEarned.text = getString(R.string.total_xp_format, result.xpEarned)
        binding.tvCoinsEarned.text = result.coinsEarned.toString()
    }

    private fun setupListeners() {
        binding.btnRetry.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnReview.setOnClickListener {
            findNavController().navigate(R.id.action_quizResultFragment_to_quizReviewFragment)
        }

        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
