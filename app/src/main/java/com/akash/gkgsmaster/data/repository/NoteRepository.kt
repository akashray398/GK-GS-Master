package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.database.NoteDao
import com.akash.gkgsmaster.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: String): NoteEntity? {
        return noteDao.getNoteById(id)
    }

    suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    private var lastDeletedNote: NoteEntity? = null

    suspend fun deleteNote(note: NoteEntity) {
        lastDeletedNote = note
        noteDao.deleteNote(note)
    }

    suspend fun undoDelete(): NoteEntity? {
        val note = lastDeletedNote
        if (note != null) {
            println("Undoing delete for note: ${note.title}")
            noteDao.insertNote(note)
            lastDeletedNote = null
        }
        return note
    }

    suspend fun createNote(title: String, content: String, category: String): NoteEntity {
        val note = NoteEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            category = category,
            lastModified = System.currentTimeMillis()
        )
        noteDao.insertNote(note)
        return note
    }

    suspend fun updateNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }
}
