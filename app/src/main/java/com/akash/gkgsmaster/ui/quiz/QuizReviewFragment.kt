package com.akash.gkgsmaster.ui.quiz

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.QuizResult
import com.akash.gkgsmaster.databinding.FragmentQuizReviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizReviewFragment : Fragment(R.layout.fragment_quiz_review) {

    private var _binding: FragmentQuizReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewAdapter
    private lateinit var numberAdapter: QuestionNumberAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizReviewBinding.bind(view)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("result", QuizResult::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("result")
        }

        result?.let { setupReview(it) }
    }

    private fun setupReview(result: QuizResult) {
        // Main Pager
        adapter = ReviewAdapter { question ->
            // Bookmark logic
            println("Bookmarking question: ${question.id}")
        }
        binding.viewPager.adapter = adapter
        
        val reviewItems = result.questions.zip(result.userAnswers)
        adapter.submitList(reviewItems)

        // Question Numbers
        numberAdapter = QuestionNumberAdapter(result.questions.size) { position ->
            binding.viewPager.currentItem = position
        }
        binding.rvQuestionNumbers.adapter = numberAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                numberAdapter.setSelected(position)
                binding.rvQuestionNumbers.smoothScrollToPosition(position)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
