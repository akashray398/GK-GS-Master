package com.akash.gkgsmaster.ui.booklet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.BookletPage
import com.akash.gkgsmaster.databinding.ItemBookletPageBinding

class BookletPageAdapter : ListAdapter<BookletPage, BookletPageAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookletPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemBookletPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookletPage) {
            binding.tvContent.text = item.content
            // Title could be passed or fetched based on page number
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BookletPage>() {
        override fun areItemsTheSame(oldItem: BookletPage, newItem: BookletPage) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BookletPage, newItem: BookletPage) = oldItem == newItem
    }
}
