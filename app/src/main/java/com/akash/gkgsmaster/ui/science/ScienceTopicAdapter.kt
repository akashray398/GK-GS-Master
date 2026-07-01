package com.akash.gkgsmaster.ui.science

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.ScienceTopic
import com.akash.gkgsmaster.databinding.ItemScienceTopicBinding

class ScienceTopicAdapter(private val onItemClick: (ScienceTopic) -> Unit) : 
    ListAdapter<ScienceTopic, ScienceTopicAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScienceTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemScienceTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScienceTopic) {
            binding.tvTopicTitle.text = item.title
            binding.tvTopicCategory.text = item.category
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScienceTopic>() {
        override fun areItemsTheSame(oldItem: ScienceTopic, newItem: ScienceTopic) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ScienceTopic, newItem: ScienceTopic) = oldItem == newItem
    }
}
