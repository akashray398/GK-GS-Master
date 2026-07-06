package com.akash.gkgsmaster.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.Achievement
import com.akash.gkgsmaster.databinding.ItemAchievementBinding

class AchievementAdapter : ListAdapter<Achievement, AchievementAdapter.AchievementViewHolder>(AchievementDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AchievementViewHolder(private val binding: ItemAchievementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(achievement: Achievement) {
            binding.tvAchievementName.text = achievement.title
            binding.ivAchievementIcon.setImageResource(achievement.iconRes)
            binding.root.alpha = if (achievement.isUnlocked) 1.0f else 0.5f
        }
    }

    class AchievementDiffCallback : DiffUtil.ItemCallback<Achievement>() {
        override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement) = oldItem == newItem
    }
}
