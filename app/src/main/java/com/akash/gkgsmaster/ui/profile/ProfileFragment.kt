package com.akash.gkgsmaster.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var achievementAdapter: AchievementAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        achievementAdapter = AchievementAdapter()
        binding.rvAchievements.adapter = achievementAdapter
    }

    private fun setupObservers() {
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.tvProfileName.text = profile.name
            binding.tvProfileEmail.text = profile.email
            binding.tvQuizScore.text = profile.totalQuizScore.toString()
            binding.tvTotalXP.text = profile.xp.toString()
            binding.tvStudyHours.text = String.format(Locale.getDefault(), "%.1fh", profile.studyHours)
            binding.tvDailyStreak.text = getString(R.string.streak_placeholder, profile.dailyStreak)
            binding.tvBookmarksCount.text = profile.bookmarksCount.toString()
            binding.tvNotesCount.text = profile.notesCount.toString()
            
            achievementAdapter.submitList(profile.achievements)
        }
    }

    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(context, "Edit Profile Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnAnalytics.setOnClickListener {
            findNavController().navigate(R.id.analyticsFragment)
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), com.akash.gkgsmaster.ui.auth.LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
