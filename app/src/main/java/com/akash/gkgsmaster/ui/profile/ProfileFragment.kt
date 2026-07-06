package com.akash.gkgsmaster.ui.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentProfileBinding
import com.akash.gkgsmaster.databinding.DialogEditProfileBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            binding.tvCoins.text = profile.coins.toString()
            binding.tvBookletsRead.text = profile.bookletsRead.toString()
            binding.tvStudyHours.text = String.format(Locale.getDefault(), "%.1fh", profile.studyHours)
            binding.tvDailyStreak.text = getString(R.string.streak_placeholder, profile.dailyStreak)
            binding.tvBookmarksCount.text = profile.bookmarksCount.toString()
            binding.tvNotesCount.text = profile.notesCount.toString()
            
            achievementAdapter.submitList(profile.achievements)
            setupProgressGraph(profile.xp) // Pass some real data or a list
        }
    }

    private fun setupProgressGraph(totalXp: Int) {
        // Simplified dynamic graph based on XP or history
        val entries = mutableListOf<Entry>()
        for (i in 0..6) {
            val value = (totalXp / 7f) * (i + 1) * (0.8f + Math.random().toFloat() * 0.4f)
            entries.add(Entry(i.toFloat(), value))
        }
        val dataSet = LineDataSet(entries, "Learning Activity").apply {
            color = Color.parseColor("#6C3BFF")
            setCircleColor(Color.WHITE)
            lineWidth = 3f
            circleRadius = 5f
            setDrawCircleHole(false)
            valueTextColor = Color.WHITE
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = Color.parseColor("#6C3BFF")
            fillAlpha = 50
        }

        binding.progressLineChart.apply {
            data = LineData(dataSet)
            xAxis.isEnabled = false
            axisLeft.textColor = Color.WHITE
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.textColor = Color.WHITE
            animateX(1500)
            invalidate()
        }
    }

    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.cvBookmarks.setOnClickListener {
            // Navigation to Bookmarks if available, else toast
            Toast.makeText(context, "Bookmarks feature coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.cvNotes.setOnClickListener {
            findNavController().navigate(R.id.notesFragment)
        }

        binding.btnAnalytics.setOnClickListener {
            findNavController().navigate(R.id.analyticsFragment)
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        binding.btnAdminPortal.setOnClickListener {
            findNavController().navigate(R.id.adminLoginFragment)
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    viewModel.logout()
                    val intent = Intent(requireContext(), com.akash.gkgsmaster.ui.auth.LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showEditProfileDialog() {
        val dialogBinding = DialogEditProfileBinding.inflate(layoutInflater)
        dialogBinding.etName.setText(binding.tvProfileName.text)
        dialogBinding.etEmail.setText(binding.tvProfileEmail.text)

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val newName = dialogBinding.etName.text.toString()
                val newEmail = dialogBinding.etEmail.text.toString()
                if (newName.isNotBlank() && newEmail.isNotBlank()) {
                    viewModel.updateProfile(newName, newEmail)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
