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
import com.akash.gkgsmaster.databinding.FragmentScienceBinding
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScienceFragment : Fragment(R.layout.fragment_science) {

    private var _binding: FragmentScienceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScienceViewModel by viewModels()
    private lateinit var adapter: ScienceCategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScienceBinding.bind(view)

        setupRecyclerView()
        setupSearch()
        setupObservers()
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchCategories(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        adapter = ScienceCategoryAdapter { category ->
            val bundle = Bundle().apply {
                putString("categoryId", category.id)
                putString("categoryName", category.name)
            }
            findNavController().navigate(R.id.action_scienceFragment_to_scienceTopicListFragment, bundle)
        }
        binding.rvCategories.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { resource ->
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
