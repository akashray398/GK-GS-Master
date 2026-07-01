package com.akash.gkgsmaster.ui.science

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentScienceTopicListBinding
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScienceTopicListFragment : Fragment(R.layout.fragment_science_topic_list) {

    private var _binding: FragmentScienceTopicListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScienceViewModel by viewModels()
    private lateinit var adapter: ScienceTopicAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScienceTopicListBinding.bind(view)

        val categoryId = arguments?.getString("categoryId") ?: ""
        val categoryName = arguments?.getString("categoryName") ?: "Science Topics"
        binding.tvTitle.text = categoryName

        setupRecyclerView()
        setupSearch()
        setupObservers()
        viewModel.loadTopics(categoryId)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchTopics(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        adapter = ScienceTopicAdapter { topic ->
            val bundle = Bundle().apply {
                putParcelable("topic", topic)
            }
            findNavController().navigate(R.id.action_scienceTopicListFragment_to_scienceTopicDetailFragment, bundle)
        }
        binding.rvTopics.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.topics.observe(viewLifecycleOwner) { resource ->
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
