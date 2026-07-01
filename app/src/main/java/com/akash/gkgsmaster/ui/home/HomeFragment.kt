package com.akash.gkgsmaster.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentHomeBinding
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    
    private lateinit var currentAffairsAdapter: CurrentAffairsAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recentActivityAdapter: RecentActivityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerViews()
        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.tvSeeAllCurrentAffairs.setOnClickListener {
            findNavController().navigate(R.id.currentAffairsFragment)
        }
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun setupRecyclerViews() {
        currentAffairsAdapter = CurrentAffairsAdapter()
        binding.rvCurrentAffairs.apply {
            adapter = currentAffairsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        categoryAdapter = CategoryAdapter { category ->
            when (category.id) {
                "1" -> findNavController().navigate(R.id.quizSetupFragment)
                "2" -> findNavController().navigate(R.id.learnFragment)
                "3" -> findNavController().navigate(R.id.scienceFragment)
                "4" -> findNavController().navigate(R.id.notesFragment)
                "5" -> findNavController().navigate(R.id.bookletListFragment)
                else -> Toast.makeText(context, "Coming Soon: ${category.title}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvCategories.adapter = categoryAdapter

        recentActivityAdapter = RecentActivityAdapter()
        binding.rvRecentActivity.apply {
            adapter = recentActivityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvUserName.text = name
        }

        viewModel.dailyQuote.observe(viewLifecycleOwner) { quote ->
            binding.tvDailyQuote.text = quote
        }

        viewModel.currentAffairs.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    currentAffairsAdapter.submitList(resource.data)
                }
                is Resource.Success -> {
                    currentAffairsAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    currentAffairsAdapter.submitList(resource.data)
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { list ->
            categoryAdapter.submitList(list)
        }

        viewModel.recentActivity.observe(viewLifecycleOwner) { list ->
            recentActivityAdapter.submitList(list)
        }

        viewModel.userProgress.observe(viewLifecycleOwner) { progress ->
            binding.tvLevelLabel.text = getString(R.string.level_placeholder, progress.level)
            binding.tvXPLabel.text = getString(R.string.xp_placeholder, progress.xp, progress.nextLevelXp)
            binding.progressXP.progress = (progress.xp * 100 / progress.nextLevelXp)
            binding.tvStreakCount.text = getString(R.string.streak_placeholder, progress.dailyStreak)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
