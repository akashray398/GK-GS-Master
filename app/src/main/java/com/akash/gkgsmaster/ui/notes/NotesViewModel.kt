package com.akash.gkgsmaster.ui.notes

import androidx.lifecycle.*
import com.akash.gkgsmaster.data.model.NoteEntity
import com.akash.gkgsmaster.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val allNotes: LiveData<List<NoteEntity>> = repository.getAllNotes().asLiveData()

    private val _searchQuery = MutableLiveData<String>("")
    val filteredNotes: LiveData<List<NoteEntity>> = _searchQuery.switchMap { query ->
        allNotes.map { list ->
            if (query.isNullOrBlank()) list
            else list.filter { it.title.contains(query, true) || it.content.contains(query, true) }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            repository.undoDelete()
            println("NotesVM: Undo Delete clicked")
        }
    }

    fun togglePin(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note.copy(isPinned = !note.isPinned, lastModified = System.currentTimeMillis()))
        }
    }

    fun toggleFavourite(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note.copy(isFavourite = !note.isFavourite, lastModified = System.currentTimeMillis()))
        }
    }

    fun saveNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }
}
