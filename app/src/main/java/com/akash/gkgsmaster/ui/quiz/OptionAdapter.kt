package com.akash.gkgsmaster.ui.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.ItemOptionBinding

class OptionAdapter(private val onOptionSelected: (Int) -> Unit) : RecyclerView.Adapter<OptionAdapter.ViewHolder>() {

    private var options: List<String> = emptyList()
    private var selectedIndex: Int = -1
    private var correctIndex: Int = -1
    private var isAnswered: Boolean = false

    fun setOptions(newOptions: List<String>) {
        options = newOptions
        selectedIndex = -1
        correctIndex = -1
        isAnswered = false
        notifyDataSetChanged()
    }

    fun showResult(selected: Int, correct: Int) {
        selectedIndex = selected
        correctIndex = correct
        isAnswered = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position], position)
    }

    override fun getItemCount() = options.size

    inner class ViewHolder(private val binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: String, position: Int) {
            binding.tvOption.text = option
            
            val context = binding.root.context
            
            when {
                !isAnswered -> {
                    binding.cardOption.setStrokeColor(ContextCompat.getColorStateList(context, R.color.glass_white))
                    binding.cardOption.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_dark))
                }
                position == correctIndex -> {
                    binding.cardOption.setStrokeColor(ContextCompat.getColorStateList(context, R.color.secondary))
                    binding.cardOption.setCardBackgroundColor(context.getColor(R.color.secondary).let { color ->
                        // Add transparency
                        (color and 0x00FFFFFF) or 0x1A000000
                    })
                }
                position == selectedIndex && selectedIndex != correctIndex -> {
                    binding.cardOption.setStrokeColor(ContextCompat.getColorStateList(context, R.color.accent))
                    binding.cardOption.setCardBackgroundColor(context.getColor(R.color.accent).let { color ->
                        (color and 0x00FFFFFF) or 0x1A000000
                    })
                }
                else -> {
                    binding.cardOption.setStrokeColor(ContextCompat.getColorStateList(context, R.color.glass_white))
                    binding.cardOption.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_dark))
                }
            }

            binding.root.setOnClickListener {
                if (!isAnswered) {
                    onOptionSelected(position)
                }
            }
        }
    }
}
