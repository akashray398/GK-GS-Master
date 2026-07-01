package com.akash.gkgsmaster.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.Achievement
import com.akash.gkgsmaster.databinding.ItemAchievementBinding

class AchievementAdapter : ListAdapter<Achievement, AchievementAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemAchievementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Achievement) {
            binding.tvAchievementTitle.text = item.title
            binding.ivAchievementIcon.setImageResource(item.iconRes)
            binding.ivAchievementIcon.alpha = if (item.isUnlocked) 1.0f else 0.4f
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Achievement>() {
        override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement) = oldItem == newItem
    }
}
