package com.akash.gkgsmaster.ui.booklet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentBookletListBinding
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookletListFragment : Fragment(R.layout.fragment_booklet_list) {

    private var _binding: FragmentBookletListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookletViewModel by viewModels()
    private lateinit var adapter: BookletAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookletListBinding.bind(view)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = BookletAdapter { booklet ->
            val bundle = Bundle().apply {
                putParcelable("booklet", booklet)
            }
            findNavController().navigate(R.id.action_bookletListFragment_to_bookletReaderFragment, bundle)
        }
        binding.rvBooklets.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.booklets.observe(viewLifecycleOwner) { resource ->
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
