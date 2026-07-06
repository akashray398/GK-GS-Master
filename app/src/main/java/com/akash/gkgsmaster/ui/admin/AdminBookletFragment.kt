package com.akash.gkgsmaster.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.databinding.FragmentAdminBookletBinding
import com.akash.gkgsmaster.ui.booklet.BookletAdapter
import com.akash.gkgsmaster.ui.booklet.BookletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminBookletFragment : Fragment(R.layout.fragment_admin_booklet) {

    private var _binding: FragmentAdminBookletBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: BookletViewModel by viewModels()
    private lateinit var adapter: BookletAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminBookletBinding.bind(view)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = BookletAdapter { booklet ->
            // Edit Booklet
            println("Admin: Clicked booklet ${booklet.title}")
            Toast.makeText(context, "Edit: ${booklet.title}", Toast.LENGTH_SHORT).show()
        }
        binding.rvBooklets.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.booklets.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is com.akash.gkgsmaster.utils.Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                else -> {}
            }
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.fabAddBooklet.setOnClickListener {
            findNavController().navigate(R.id.action_adminBookletFragment_to_adminAddBookletFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
