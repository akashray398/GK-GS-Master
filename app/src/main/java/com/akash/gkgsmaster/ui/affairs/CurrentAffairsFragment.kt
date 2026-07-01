package com.akash.gkgsmaster.ui.affairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import com.akash.gkgsmaster.databinding.FragmentCurrentAffairsBinding
import com.akash.gkgsmaster.utils.Resource
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentAffairsFragment : Fragment(R.layout.fragment_current_affairs) {

    private var _binding: FragmentCurrentAffairsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrentAffairsViewModel by viewModels()
    private lateinit var adapter: CurrentAffairAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrentAffairsBinding.bind(view)

        setupRecyclerView()
        setupChips()
        setupRefresh()
        setupObservers()
        
        viewModel.loadCurrentAffairs()
    }

    private fun setupRecyclerView() {
        adapter = CurrentAffairAdapter(
            onBookmarkClick = { item ->
                viewModel.toggleBookmark(item.id, !item.isBookmarked)
            },
            onShareClick = { item ->
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${item.title}\n\n${item.content}")
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Share Current Affair"))
            },
            onItemClick = { item ->
                // Navigate to detail if needed
                Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvCurrentAffairs.adapter = adapter
    }

    private fun setupChips() {
        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = checkedIds.firstOrNull()
            if (checkedId != null) {
                val chip = group.findViewById<Chip>(checkedId)
                val category = if (chip.text == "All") null else chip.text.toString()
                viewModel.loadCurrentAffairs(category)
            }
        }
    }

    private fun setupRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupObservers() {
        viewModel.currentAffairs.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = if (resource.data == null) View.VISIBLE else View.GONE
                    binding.swipeRefresh.isRefreshing = true
                    adapter.submitList(resource.data)
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    adapter.submitList(resource.data)
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
