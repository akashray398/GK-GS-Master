package com.akash.gkgsmaster.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.SearchResult
import com.akash.gkgsmaster.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchResultAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        setupRecyclerView()
        setupSearchView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = SearchResultAdapter { result ->
            handleSearchResultClick(result)
        }
        binding.rvSearchResults.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                return true
            }
        })
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            adapter.submitList(results)
            binding.tvEmptyState.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
            binding.rvSearchResults.visibility = if (results.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun handleSearchResultClick(result: SearchResult) {
        when (result) {
            is SearchResult.GKArticleResult -> {
                // Navigate to GK Article (need the full article object or just id)
                // For simplicity, we can pass a bundle with basic info or just id
                val bundle = Bundle().apply {
                    putString("categoryId", result.category)
                    putString("categoryName", result.category)
                }
                findNavController().navigate(R.id.action_searchFragment_to_GKArticleListFragment, bundle)
            }
            is SearchResult.ScienceTopicResult -> {
                val bundle = Bundle().apply {
                    putString("categoryId", result.category)
                    putString("categoryName", result.category)
                }
                findNavController().navigate(R.id.action_searchFragment_to_scienceTopicListFragment, bundle)
            }
            is SearchResult.BookletResult -> {
                findNavController().navigate(R.id.bookletListFragment)
            }
            is SearchResult.CurrentAffairResult -> {
                findNavController().navigate(R.id.currentAffairsFragment)
            }
            is SearchResult.QuestionResult -> {
                findNavController().navigate(R.id.quizSetupFragment)
            }
            is SearchResult.NoteResult -> {
                findNavController().navigate(R.id.notesFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
