package com.akash.gkgsmaster.ui.quiz

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.databinding.ItemQuizOptionBinding

class OptionsAdapter(
    private val onOptionSelected: (Int) -> Unit
) : RecyclerView.Adapter<OptionsAdapter.OptionViewHolder>() {

    private var options: List<String> = emptyList()
    private var selectedIndex: Int = -1
    private var correctIndex: Int = -1
    private var isAnswered: Boolean = false

    fun setOptions(newOptions: List<String>, correctIdx: Int) {
        val oldSize = options.size
        options = newOptions
        correctIndex = correctIdx
        selectedIndex = -1
        isAnswered = false
        if (oldSize == newOptions.size) {
            notifyItemRangeChanged(0, options.size)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemQuizOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position], position)
    }

    override fun getItemCount() = options.size

    inner class OptionViewHolder(private val binding: ItemQuizOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: String, position: Int) {
            binding.tvOption.text = option
            
            // Reset state
            binding.cvOption.setStrokeColor("#1AFFFFFF".toColorInt())
            binding.cvOption.setCardBackgroundColor("#1E1E1E".toColorInt())

            if (isAnswered) {
                if (position == correctIndex) {
                    binding.cvOption.setStrokeColor(Color.GREEN)
                    binding.cvOption.setCardBackgroundColor("#1A00FF00".toColorInt())
                } else if (position == selectedIndex) {
                    binding.cvOption.setStrokeColor(Color.RED)
                    binding.cvOption.setCardBackgroundColor("#1AFF0000".toColorInt())
                }
            } else {
                if (position == selectedIndex) {
                    binding.cvOption.setStrokeColor("#6C3BFF".toColorInt())
                }
            }

            binding.root.setOnClickListener {
                if (!isAnswered) {
                    val oldIndex = selectedIndex
                    selectedIndex = position
                    if (oldIndex != -1) notifyItemChanged(oldIndex)
                    notifyItemChanged(selectedIndex)
                    onOptionSelected(position)
                }
            }
        }
    }
}
