package com.akash.gkgsmaster.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.SearchResult
import com.akash.gkgsmaster.databinding.ItemSearchResultBinding

class SearchResultAdapter(
    private val onItemClick: (SearchResult) -> Unit,
) : ListAdapter<SearchResult, SearchResultAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: SearchResult) {
            binding.tvTitle.text = result.title
            binding.tvType.text = result.type.name
            binding.root.setOnClickListener { onItemClick(result) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) = oldItem == newItem
    }
}
