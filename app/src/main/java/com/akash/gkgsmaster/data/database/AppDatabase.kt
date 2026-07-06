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
        BookmarkEntity::class,
        LearningTopicEntity::class,
        RecentActivityEntity::class,
        AdminUser::class,
        AdminActivity::class,
        QuizHistory::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun currentAffairDao(): CurrentAffairDao
    abstract fun bookletDao(): BookletDao
    abstract fun noteDao(): NoteDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun learningTopicDao(): LearningTopicDao
    abstract fun recentActivityDao(): RecentActivityDao
    abstract fun adminDao(): AdminDao
    abstract fun quizHistoryDao(): QuizHistoryDao
}
