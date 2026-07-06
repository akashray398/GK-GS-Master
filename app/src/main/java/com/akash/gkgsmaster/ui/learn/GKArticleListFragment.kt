package com.akash.gkgsmaster.ui.learn

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentGkArticleListBinding
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GKArticleListFragment : Fragment(R.layout.fragment_gk_article_list) {

    private var _binding: FragmentGkArticleListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GKViewModel by viewModels()
    private lateinit var adapter: GKArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGkArticleListBinding.bind(view)

        val categoryId = arguments?.getString("categoryId") ?: ""
        val categoryName = arguments?.getString("categoryName") ?: "Articles"
        binding.tvTitle.text = categoryName

        setupRecyclerView()
        setupSearch()
        setupObservers()
        viewModel.loadArticles(categoryId)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchArticles(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        adapter = GKArticleAdapter { article ->
            val bundle = Bundle().apply {
                putString("topicId", article.id)
            }
            findNavController().navigate(R.id.action_GKArticleListFragment_to_learningDetailFragment, bundle)
        }
        binding.rvArticles.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.articles.observe(viewLifecycleOwner) { resource ->
            println("GKArticleList: Observed ${resource.javaClass.simpleName}")
            when (resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
