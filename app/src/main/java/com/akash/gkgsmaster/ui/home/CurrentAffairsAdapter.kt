package com.akash.gkgsmaster.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import com.akash.gkgsmaster.databinding.ItemCurrentAffairBinding

class CurrentAffairsAdapter(
    private val onItemClick: (CurrentAffairEntity) -> Unit
) : ListAdapter<CurrentAffairEntity, CurrentAffairsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrentAffairBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemCurrentAffairBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CurrentAffairEntity) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = item.date
            binding.ivCover.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_error)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CurrentAffairEntity>() {
        override fun areItemsTheSame(oldItem: CurrentAffairEntity, newItem: CurrentAffairEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CurrentAffairEntity, newItem: CurrentAffairEntity) = oldItem == newItem
    }
}
