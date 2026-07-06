package com.akash.gkgsmaster.ui.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.ItemQuestionNumberBinding

class QuestionNumberAdapter(
    private val count: Int,
    private val onNumberClick: (Int) -> Unit
) : RecyclerView.Adapter<QuestionNumberAdapter.ViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuestionNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = count

    fun setSelected(position: Int) {
        val old = selectedPosition
        selectedPosition = position
        notifyItemChanged(old)
        notifyItemChanged(selectedPosition)
    }

    inner class ViewHolder(private val binding: ItemQuestionNumberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val numText = (position + 1).toString()
            binding.tvNumber.text = numText
            
            val isSelected = position == selectedPosition
            println("Question Number $numText is selected: $isSelected")
            binding.cvNumber.setStrokeColor(
                if (isSelected) ContextCompat.getColor(binding.root.context, R.color.primary)
                else ContextCompat.getColor(binding.root.context, android.R.color.transparent)
            )
            
            binding.root.setOnClickListener { onNumberClick(position) }
        }
    }
}
