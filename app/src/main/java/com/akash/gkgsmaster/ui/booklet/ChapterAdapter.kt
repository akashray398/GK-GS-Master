package com.akash.gkgsmaster.ui.booklet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.Chapter
import com.akash.gkgsmaster.databinding.ItemChapterBinding

class ChapterAdapter(private val onItemClick: (Chapter) -> Unit) : 
    ListAdapter<Chapter, ChapterAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemChapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Chapter) {
            binding.tvOrder.text = item.order.toString()
            binding.tvChapterTitle.text = item.title
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter) = oldItem == newItem
    }
}
