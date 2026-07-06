package com.akash.gkgsmaster.ui.learn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentLearnBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearnFragment : Fragment(R.layout.fragment_learn) {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLearnBinding.bind(view)

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = LearnPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Recommended Books"
                1 -> "Study Material"
                else -> null
            }
        }.attach()
        
        val showRecommended = arguments?.getBoolean("showRecommended") ?: false
        println("LearnFragment: showRecommended=$showRecommended")
        if (!showRecommended) {
            binding.viewPager.setCurrentItem(1, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
