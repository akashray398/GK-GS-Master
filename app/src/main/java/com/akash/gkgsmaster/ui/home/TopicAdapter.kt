package com.akash.gkgsmaster.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.LearningTopicEntity
import com.akash.gkgsmaster.databinding.ItemCategoryBinding

class TopicAdapter(
    private val onTopicClick: (LearningTopicEntity) -> Unit
) : ListAdapter<LearningTopicEntity, TopicAdapter.TopicViewHolder>(TopicDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TopicViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: LearningTopicEntity) {
            binding.tvTitle.text = topic.title
            // You can use a specific icon or a default one
            // binding.ivIcon.setImageResource(R.drawable.ic_placeholder)
            binding.root.setOnClickListener { onTopicClick(topic) }
        }
    }

    class TopicDiffCallback : DiffUtil.ItemCallback<LearningTopicEntity>() {
        override fun areItemsTheSame(oldItem: LearningTopicEntity, newItem: LearningTopicEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LearningTopicEntity, newItem: LearningTopicEntity) = oldItem == newItem
    }
}
