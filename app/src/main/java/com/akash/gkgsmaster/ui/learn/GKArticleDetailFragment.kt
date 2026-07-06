package com.akash.gkgsmaster.ui.learn

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.GKArticle
import com.akash.gkgsmaster.databinding.FragmentGkArticleDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GKArticleDetailFragment : Fragment(R.layout.fragment_gk_article_detail) {

    private var _binding: FragmentGkArticleDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGkArticleDetailBinding.bind(view)

        val article = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("article", GKArticle::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("article")
        }

        println("GKArticleDetail: article=${article?.title}")
        article?.let { setupUI(it) }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupUI(article: GKArticle) {
        println("Displaying article: ${article.title}")
        binding.tvTitle.text = article.title
        binding.tvContent.text = article.content
        binding.tvCategory.text = article.category.uppercase()
        
        article.highlights?.let {
            binding.tvHighlights.visibility = View.VISIBLE
            binding.tvHighlights.text = it
        } ?: run {
            binding.tvHighlights.visibility = View.GONE
        }

        binding.btnBookmark.setOnClickListener {
            Toast.makeText(context, "Bookmarked", Toast.LENGTH_SHORT).show()
        }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.content}")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share GK Article"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
