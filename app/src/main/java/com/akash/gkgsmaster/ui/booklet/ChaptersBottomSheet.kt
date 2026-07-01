package com.akash.gkgsmaster.ui.booklet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.data.model.Chapter
import com.akash.gkgsmaster.databinding.BottomSheetChaptersBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChaptersBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetChaptersBinding? = null
    private val binding get() = _binding!!
    private var booklet: Booklet? = null
    private var onChapterClick: ((Chapter) -> Unit)? = null

    companion object {
        fun newInstance(booklet: Booklet, onChapterClick: (Chapter) -> Unit): ChaptersBottomSheet {
            return ChaptersBottomSheet().apply {
                this.booklet = booklet
                this.onChapterClick = onChapterClick
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetChaptersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val adapter = ChapterAdapter { chapter ->
            onChapterClick?.invoke(chapter)
            dismiss()
        }
        binding.rvChapters.adapter = adapter
        adapter.submitList(booklet?.chapters)
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
