package com.akash.gkgsmaster.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.data.model.NoteEntity
import com.akash.gkgsmaster.databinding.ItemNoteBinding

class NotesAdapter(
    private val onNoteClick: (NoteEntity) -> Unit,
    private val onPinClick: (NoteEntity) -> Unit,
    private val onFavouriteClick: (NoteEntity) -> Unit,
    private val onDeleteClick: (NoteEntity) -> Unit
) : ListAdapter<NoteEntity, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteEntity) {
            binding.tvNoteTitle.text = note.title
            binding.tvNoteContent.text = note.content
            binding.tvCategory.text = note.category
            binding.ivPin.visibility = if (note.isPinned) View.VISIBLE else View.GONE
            
            binding.root.setOnClickListener { onNoteClick(note) }
            binding.root.setOnLongClickListener {
                onDeleteClick(note)
                true
            }
        }
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity) = oldItem == newItem
    }
}
