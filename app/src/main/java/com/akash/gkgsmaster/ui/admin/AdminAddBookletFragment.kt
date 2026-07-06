package com.akash.gkgsmaster.ui.admin

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.databinding.FragmentAdminAddBookletBinding
import com.akash.gkgsmaster.ui.booklet.BookletViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@AndroidEntryPoint
class AdminAddBookletFragment : Fragment(R.layout.fragment_admin_add_booklet) {

    private var _binding: FragmentAdminAddBookletBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: BookletViewModel by viewModels()
    private var selectedPdfUri: Uri? = null

    private val selectPdfLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedPdfUri = it
            binding.btnUploadPdf.text = getString(R.string.pdf_selected_format, getFileName(it))
            Toast.makeText(context, "PDF Selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminAddBookletBinding.bind(view)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.btnUploadPdf.setOnClickListener {
            selectPdfLauncher.launch("application/pdf")
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val author = binding.etAuthor.text.toString()
            val category = binding.etCategory.text.toString()
            val desc = binding.etDescription.text.toString()

            if (title.isBlank() || author.isBlank()) {
                Toast.makeText(context, "Title and Author are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var localPdfPath: String? = null
            selectedPdfUri?.let { uri ->
                localPdfPath = savePdfToInternalStorage(uri, title)
            }

            val booklet = Booklet(
                id = UUID.randomUUID().toString(),
                title = title,
                author = author,
                category = category,
                description = desc,
                totalChapters = 0,
                progress = 0,
                pdfUrl = localPdfPath,
                isDownloaded = localPdfPath != null
            )

            viewModel.insertBooklet(booklet)
            Toast.makeText(context, "Booklet Created Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun savePdfToInternalStorage(uri: Uri, title: String): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val fileName = "${title.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(uri: Uri): String {
        return uri.path?.substringAfterLast('/') ?: "document.pdf"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
