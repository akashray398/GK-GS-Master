package com.akash.gkgsmaster.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val lastModified: Long,
    val isPinned: Boolean = false,
    val isFavourite: Boolean = false,
    val isSynced: Boolean = false,
    val imageUrls: List<String> = emptyList(),
    val voiceNoteUrl: String? = null,
    val hasChecklist: Boolean = false
) : Parcelable
