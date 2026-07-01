package com.akash.gkgsmaster.ui.learn

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.GKArticle
import com.akash.gkgsmaster.databinding.ItemGkArticleBinding

class GKArticleAdapter(private val onItemClick: (GKArticle) -> Unit) : 
    ListAdapter<GKArticle, GKArticleAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGkArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemGkArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GKArticle) {
            binding.tvArticleTitle.text = item.title
            binding.tvArticleCategory.text = item.category
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GKArticle>() {
        override fun areItemsTheSame(oldItem: GKArticle, newItem: GKArticle) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: GKArticle, newItem: GKArticle) = oldItem == newItem
    }
}
