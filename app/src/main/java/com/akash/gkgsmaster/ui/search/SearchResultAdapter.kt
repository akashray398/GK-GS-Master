package com.akash.gkgsmaster.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.SearchResult
import com.akash.gkgsmaster.data.model.SearchResultType
import com.akash.gkgsmaster.databinding.ItemSearchResultBinding

class SearchResultAdapter(private val onItemClick: (SearchResult) -> Unit) :
    ListAdapter<SearchResult, SearchResultAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResult) {
            binding.tvResultTitle.text = item.title
            binding.tvResultType.text = item.type.name
            
            binding.tvResultSubtitle.text = when (item) {
                is SearchResult.GKArticleResult -> "Category: ${item.category}"
                is SearchResult.ScienceTopicResult -> "Category: ${item.category}"
                is SearchResult.BookletResult -> "Author: ${item.author}"
                is SearchResult.CurrentAffairResult -> "Date: ${item.date}"
                is SearchResult.QuestionResult -> "Category: ${item.category}"
                is SearchResult.NoteResult -> "Note"
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem.id == newItem.id && oldItem.type == newItem.type
        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) = oldItem == newItem
    }
}
