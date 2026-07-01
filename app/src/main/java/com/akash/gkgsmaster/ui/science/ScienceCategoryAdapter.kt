package com.akash.gkgsmaster.ui.science

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.ScienceCategory
import com.akash.gkgsmaster.databinding.ItemCategoryBinding

class ScienceCategoryAdapter(private val onItemClick: (ScienceCategory) -> Unit) : 
    ListAdapter<ScienceCategory, ScienceCategoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScienceCategory) {
            binding.tvTitle.text = item.name
            binding.ivIcon.setImageResource(item.iconRes)
            binding.ivIcon.setColorFilter(binding.root.context.getColor(item.colorRes))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScienceCategory>() {
        override fun areItemsTheSame(oldItem: ScienceCategory, newItem: ScienceCategory) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ScienceCategory, newItem: ScienceCategory) = oldItem == newItem
    }
}
