package com.akash.gkgsmaster.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.HomeCategory
import com.akash.gkgsmaster.databinding.ItemCategoryBinding

class CategoryAdapter(private val onItemClick: (HomeCategory) -> Unit) : ListAdapter<HomeCategory, CategoryAdapter.ViewHolder>(DiffCallback()) {

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
        fun bind(item: HomeCategory) {
            val context = binding.root.context
            binding.tvTitle.text = item.title
            binding.ivIcon.setImageResource(item.iconRes)
            
            val color = context.getColor(item.colorRes)
            binding.ivIcon.setColorFilter(color)
            
            // Add a subtle background glow
            val alphaColor = androidx.core.graphics.ColorUtils.setAlphaComponent(color, 40)
            binding.cardCategory.setCardBackgroundColor(alphaColor)
            binding.cardCategory.strokeColor = color
            binding.cardCategory.strokeWidth = 2
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeCategory>() {
        override fun areItemsTheSame(oldItem: HomeCategory, newItem: HomeCategory) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: HomeCategory, newItem: HomeCategory) = oldItem == newItem
    }
}
