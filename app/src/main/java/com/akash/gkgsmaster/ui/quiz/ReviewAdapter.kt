package com.akash.gkgsmaster.ui.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.Question
import com.akash.gkgsmaster.databinding.ItemQuizReviewBinding

class ReviewAdapter(
    private val onBookmarkClick: (Question) -> Unit
) : ListAdapter<Pair<Question, Int>, ReviewAdapter.ViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuizReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.first, item.second, position)
    }

    inner class ViewHolder(private val binding: ItemQuizReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question, userAnswerIndex: Int, position: Int) {
            val context = binding.root.context
            binding.tvQuestionNumber.text = context.getString(R.string.question_count_format, position + 1, itemCount)
            binding.tvQuestion.text = question.text
            
            binding.optionsContainer.removeAllViews()
            
            question.options.forEachIndexed { index, option ->
                val optionBinding = com.akash.gkgsmaster.databinding.ItemQuizOptionBinding.inflate(
                    LayoutInflater.from(context), binding.optionsContainer, false
                )
                optionBinding.tvOption.text = option
                
                val isCorrect = index == question.correctOptionIndex
                val isSelected = index == userAnswerIndex
                
                when {
                    isCorrect -> {
                        optionBinding.cvOption.setStrokeColor(ContextCompat.getColor(context, R.color.success))
                        optionBinding.cvOption.setCardBackgroundColor(ContextCompat.getColor(context, R.color.glass_primary))
                    }
                    isSelected && !isCorrect -> {
                        optionBinding.cvOption.setStrokeColor(ContextCompat.getColor(context, R.color.error))
                    }
                }
                
                binding.optionsContainer.addView(optionBinding.root)
            }
            
            binding.tvExplanation.text = question.explanation
            binding.tvWhyCorrect.text = context.getString(R.string.why_correct_format, question.whyCorrect)
            binding.tvWhyIncorrect.text = context.getString(R.string.others_format, question.whyOthersIncorrect)
            binding.tvUpscTip.text = context.getString(R.string.upsc_tip_format, question.upscTip)
            
            binding.btnBookmark.setImageResource(
                if (question.isBookmarked) android.R.drawable.btn_star_big_on 
                else android.R.drawable.btn_star_big_off
            )
            
            binding.btnBookmark.setOnClickListener {
                onBookmarkClick(question)
            }
        }
    }

    class ReviewDiffCallback : DiffUtil.ItemCallback<Pair<Question, Int>>() {
        override fun areItemsTheSame(oldItem: Pair<Question, Int>, newItem: Pair<Question, Int>) = 
            oldItem.first.id == newItem.first.id
        override fun areContentsTheSame(oldItem: Pair<Question, Int>, newItem: Pair<Question, Int>) = 
            oldItem == newItem
    }
}
