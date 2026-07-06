package com.akash.gkgsmaster.ui.notes

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentNoteDetailBinding
import com.akash.gkgsmaster.utils.PdfExporter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModels()
    private var noteId: String? = null
    private var isInitialLoad = true
    private var currentNote: com.akash.gkgsmaster.data.model.NoteEntity? = null

    private val voiceRecognitionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = data?.get(0)
            if (!spokenText.isNullOrBlank()) {
                val currentText = binding.etContent.text.toString()
                binding.etContent.setText(if (currentText.isEmpty()) spokenText else "$currentText $spokenText")
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            Toast.makeText(context, "Image attached: $it", Toast.LENGTH_SHORT).show()
            // In a real app, we would add this URI to the note's imageUrls list
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your note...")
        }
        try {
            voiceRecognitionLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Voice recognition not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImage() {
        pickImageLauncher.launch("image/*")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNoteDetailBinding.bind(view)

        noteId = arguments?.getString("noteId") ?: java.util.UUID.randomUUID().toString()
        val isNewNote = arguments?.getString("noteId") == null
        
        if (!isNewNote) {
            setupEditMode()
        }

        setupClickListeners()
        setupAutoSave()
    }

    private fun setupAutoSave() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                saveNoteLocally()
            }
        }
        binding.etTitle.addTextChangedListener(textWatcher)
        binding.etContent.addTextChangedListener(textWatcher)
        binding.etCategory.addTextChangedListener(textWatcher)
    }

    private fun saveNoteLocally() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()
        val category = binding.etCategory.text.toString()
        
        if (title.isNotBlank() || content.isNotBlank()) {
            val note = currentNote?.copy(
                title = title,
                content = content,
                category = category,
                lastModified = System.currentTimeMillis()
            ) ?: com.akash.gkgsmaster.data.model.NoteEntity(
                id = noteId!!,
                title = title,
                content = content,
                category = category,
                lastModified = System.currentTimeMillis()
            )
            currentNote = note
            viewModel.saveNote(note)
        }
    }

    private fun setupEditMode() {
        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            if (isInitialLoad) {
                val note = notes.find { it.id == noteId }
                note?.let {
                    currentNote = it
                    binding.etTitle.setText(it.title)
                    binding.etContent.setText(it.content)
                    binding.etCategory.setText(it.category)
                    isInitialLoad = false
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnVoiceNote.setOnClickListener {
            startVoiceRecognition()
        }

        binding.btnAttachImage.setOnClickListener {
            pickImage()
        }

        binding.btnChecklist.setOnClickListener {
            Toast.makeText(context, "Checklist feature coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val category = binding.etCategory.text.toString()

            if (title.isBlank() && content.isBlank()) {
                Toast.makeText(context, "Cannot save empty note", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = currentNote?.copy(
                title = title,
                content = content,
                category = category,
                lastModified = System.currentTimeMillis()
            ) ?: com.akash.gkgsmaster.data.model.NoteEntity(
                id = noteId!!,
                title = title,
                content = content,
                category = category,
                lastModified = System.currentTimeMillis()
            )
            viewModel.saveNote(note)
            findNavController().navigateUp()
        }

        binding.btnExportPdf.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val category = binding.etCategory.text.toString()
            
            println("Notes: Exporting PDF...")
            val note = com.akash.gkgsmaster.data.model.NoteEntity(
                id = noteId ?: "temp",
                title = title,
                content = content,
                category = category,
                lastModified = System.currentTimeMillis()
            )
            
            val file = PdfExporter.exportNoteToPdf(requireContext(), note)
            if (file != null) {
                Toast.makeText(context, "PDF exported to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                // Optionally open the PDF
            } else {
                Toast.makeText(context, "Failed to export PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
