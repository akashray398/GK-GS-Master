package com.akash.gkgsmaster.ui.learn

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.LearningTopicEntity
import com.akash.gkgsmaster.databinding.FragmentLearningDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearningDetailFragment : Fragment(R.layout.fragment_learning_detail) {

    private var _binding: FragmentLearningDetailBinding? = null
    private val binding get() = _binding!!
    
    // We can use GKViewModel or a specific LearningViewModel
    private val viewModel: GKViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLearningDetailBinding.bind(view)

        setupObservers()

        val topicId = arguments?.getString("topicId")
        topicId?.let { id ->
            println("LearningDetailFragment: Loading topic $id")
            viewModel.loadTopicById(id)
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObservers() {
        viewModel.currentTopic.observe(viewLifecycleOwner) { topic ->
            topic?.let { setupUI(it) }
        }
    }

    private fun setupUI(topic: LearningTopicEntity) {
        binding.tvTitle.text = topic.title
        binding.tvContent.text = topic.content
        binding.tvHighlights.text = topic.highlights
        binding.tvImportantFacts.text = topic.importantFacts.joinToString("\n• ", prefix = "• ")
        binding.tvBulletPoints.text = topic.bulletPoints.joinToString("\n• ", prefix = "• ")
        
        binding.btnBookmark.setOnClickListener {
            viewModel.toggleBookmark(topic.id, !topic.isBookmarked)
            val msg = if (!topic.isBookmarked) "Bookmarked" else "Removed from Bookmarks"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        binding.btnShare.setOnClickListener {
            val shareIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                putExtra(android.content.Intent.EXTRA_TEXT, "${topic.title}\n\n${topic.content}")
                type = "text/plain"
            }
            startActivity(android.content.Intent.createChooser(shareIntent, "Share Topic"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
