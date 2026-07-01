package com.akash.gkgsmaster.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.akash.gkgsmaster.data.model.*

@Database(
    entities = [
        Question::class,
        CurrentAffairEntity::class,
        Booklet::class,
        NoteEntity::class,
        BookmarkEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun currentAffairDao(): CurrentAffairDao
    abstract fun bookletDao(): BookletDao
    abstract fun noteDao(): NoteDao
    abstract fun bookmarkDao(): BookmarkDao
}
