package com.akash.gkgsmaster.ui.notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentNotesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes) {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(
            onNoteClick = { note ->
                // Navigate to Edit Note
                val bundle = Bundle().apply {
                    putString("noteId", note.id)
                }
                findNavController().navigate(R.id.noteDetailFragment, bundle)
            },
            onPinClick = { viewModel.togglePin(it) },
            onFavouriteClick = { viewModel.toggleFavourite(it) },
            onDeleteClick = { note ->
                viewModel.deleteNote(note)
                com.google.android.material.snackbar.Snackbar.make(binding.root, "Note deleted", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                    .setAction("Undo") { viewModel.undoDelete() }
                    .show()
            }
        )
        binding.rvNotes.apply {
            adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun setupObservers() {
        viewModel.filteredNotes.observe(viewLifecycleOwner) { notes ->
            notesAdapter.submitList(notes)
            binding.tvEmptyState.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.noteDetailFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
