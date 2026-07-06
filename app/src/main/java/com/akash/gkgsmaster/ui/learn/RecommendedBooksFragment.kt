package com.akash.gkgsmaster.ui.learn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.BookletType
import com.akash.gkgsmaster.databinding.FragmentBookletListBinding
import com.akash.gkgsmaster.ui.booklet.BookletAdapter
import com.akash.gkgsmaster.ui.booklet.BookletViewModel
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendedBooksFragment : Fragment(R.layout.fragment_booklet_list) {

    private var _binding: FragmentBookletListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookletViewModel by viewModels()
    private lateinit var adapter: BookletAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookletListBinding.bind(view)

        binding.tvHeader.visibility = View.GONE

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = BookletAdapter { booklet ->
            val bundle = Bundle().apply {
                putParcelable("booklet", booklet)
            }
            if (booklet.type == BookletType.RECOMMENDED_BOOK) {
                findNavController().navigate(R.id.action_learnFragment_to_bookDetailFragment, bundle)
            } else {
                findNavController().navigate(R.id.bookletReaderFragment, bundle)
            }
        }
        binding.rvBooklets.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.booklets.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val filtered = resource.data?.filter { it.type == BookletType.RECOMMENDED_BOOK } ?: emptyList()
                    adapter.submitList(filtered)
                    println("Recommended Books: Displaying ${filtered.size} books")
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    println("Recommended Books: Error ${resource.message}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
