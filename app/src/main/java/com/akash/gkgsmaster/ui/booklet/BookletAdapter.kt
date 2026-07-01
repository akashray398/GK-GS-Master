package com.akash.gkgsmaster.ui.booklet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.databinding.ItemBookletBinding
import com.akash.gkgsmaster.R

class BookletAdapter(private val onItemClick: (Booklet) -> Unit) : 
    ListAdapter<Booklet, BookletAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemBookletBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Booklet) {
            val context = binding.root.context
            binding.tvTitle.text = item.title
            binding.tvAuthor.text = context.getString(R.string.author_placeholder, item.author)
            binding.tvProgressLabel.text = context.getString(R.string.progress_placeholder, item.progress)
            binding.progressIndicator.progress = item.progress
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Booklet>() {
        override fun areItemsTheSame(oldItem: Booklet, newItem: Booklet) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Booklet, newItem: Booklet) = oldItem == newItem
    }
}
