package com.akash.gkgsmaster.ui.learn

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LearnPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecommendedBooksFragment()
            1 -> StudyMaterialFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
