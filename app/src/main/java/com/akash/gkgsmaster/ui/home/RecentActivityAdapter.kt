package com.akash.gkgsmaster.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.RecentActivity
import com.akash.gkgsmaster.databinding.ItemRecentActivityBinding

class RecentActivityAdapter : ListAdapter<RecentActivity, RecentActivityAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRecentActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecentActivity) {
            binding.tvTitle.text = item.title
            binding.tvTime.text = item.timestamp
            binding.tvScore.text = item.score
            binding.tvScore.visibility = if (item.score != null) View.VISIBLE else View.GONE
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RecentActivity>() {
        override fun areItemsTheSame(oldItem: RecentActivity, newItem: RecentActivity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RecentActivity, newItem: RecentActivity) = oldItem == newItem
    }
}
