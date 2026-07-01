package com.akash.gkgsmaster.ui.booklet

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.data.model.BookletPage
import com.akash.gkgsmaster.databinding.FragmentBookletReaderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookletReaderFragment : Fragment(R.layout.fragment_booklet_reader) {

    private var _binding: FragmentBookletReaderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookletViewModel by viewModels()
    private lateinit var adapter: BookletPageAdapter
    private var booklet: Booklet? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookletReaderBinding.bind(view)

        booklet = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("booklet", Booklet::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("booklet")
        }

        booklet?.let { setupUI(it) }
        
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupUI(booklet: Booklet) {
        binding.tvBookletTitle.text = booklet.title
        
        adapter = BookletPageAdapter()
        binding.viewPager.adapter = adapter
        
        val allPages = booklet.chapters.flatMap { it.pages }
        adapter.submitList(allPages)
        
        binding.tvPageIndicator.text = getString(R.string.page_indicator_placeholder, 1, allPages.size)
        binding.seekBarProgress.max = allPages.size - 1
        
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tvPageIndicator.text = getString(R.string.page_indicator_placeholder, position + 1, allPages.size)
                binding.seekBarProgress.progress = position
            }
        })
        
        binding.seekBarProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) binding.viewPager.currentItem = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnFavorite.setOnClickListener {
            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
        }

        binding.btnBookmark.setOnClickListener {
            Toast.makeText(context, "Page bookmarked", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnChapters.setOnClickListener {
            // Show Chapters Bottom Sheet
            val bottomSheet = ChaptersBottomSheet.newInstance(booklet) { chapter ->
                val pageIndex = allPages.indexOfFirst { it.id == chapter.pages.firstOrNull()?.id }
                if (pageIndex != -1) {
                    binding.viewPager.currentItem = pageIndex
                }
            }
            bottomSheet.show(childFragmentManager, "ChaptersBottomSheet")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
