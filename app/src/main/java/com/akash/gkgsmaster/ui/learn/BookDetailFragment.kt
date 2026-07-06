package com.akash.gkgsmaster.ui.learn

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.databinding.FragmentBookDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailFragment : Fragment(R.layout.fragment_book_detail) {

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookDetailBinding.bind(view)

        val booklet = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("booklet", Booklet::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("booklet")
        }

        println("BookDetailFragment: booklet=${booklet?.title}")
        booklet?.let { setupUI(it) }
        
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupUI(booklet: Booklet) {
        binding.tvBookName.text = booklet.title
        binding.tvAuthor.text = getString(R.string.author_placeholder, booklet.author)
        binding.tvSubject.text = getString(R.string.subject_format, booklet.subject ?: booklet.category)
        binding.tvEdition.text = getString(R.string.edition_format, booklet.edition ?: "Latest")
        binding.tvDescription.text = booklet.description
        binding.tvRelevance.text = booklet.upscRelevance
        binding.tvTopics.text = booklet.topicsCovered.joinToString(", ")

        if (booklet.pdfUrl != null || booklet.isDownloaded) {
            binding.btnAction.text = "Read Book"
            binding.btnAction.setOnClickListener {
                val bundle = Bundle().apply { putParcelable("booklet", booklet) }
                findNavController().navigate(R.id.bookletReaderFragment, bundle)
            }
        } else {
            binding.btnAction.text = "Available through Admin"
            binding.btnAction.setOnClickListener {
                Toast.makeText(context, "Please contact Admin to access this PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
