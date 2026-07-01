package com.akash.gkgsmaster.ui.affairs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import com.akash.gkgsmaster.databinding.ItemCurrentAffairListBinding

class CurrentAffairAdapter(
    private val onBookmarkClick: (CurrentAffairEntity) -> Unit,
    private val onShareClick: (CurrentAffairEntity) -> Unit,
    private val onItemClick: (CurrentAffairEntity) -> Unit
) : ListAdapter<CurrentAffairEntity, CurrentAffairAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrentAffairListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemCurrentAffairListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CurrentAffairEntity) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = item.date
            binding.tvCategory.text = item.category
            
            val bookmarkIcon = if (item.isBookmarked) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
            binding.btnBookmark.setImageResource(bookmarkIcon)

            binding.btnBookmark.setOnClickListener { onBookmarkClick(item) }
            binding.btnShare.setOnClickListener { onShareClick(item) }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CurrentAffairEntity>() {
        override fun areItemsTheSame(oldItem: CurrentAffairEntity, newItem: CurrentAffairEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CurrentAffairEntity, newItem: CurrentAffairEntity) = oldItem == newItem
    }
}
