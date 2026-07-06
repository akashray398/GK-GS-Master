package com.akash.gkgsmaster.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.akash.gkgsmaster.data.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        var database: AppDatabase? = null
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "gk_gs_master_db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        database?.bookletDao()?.insertBooklets(DatabaseInitializer.getInitialBooklets())
                        database?.questionDao()?.insertQuestions(DatabaseInitializer.getInitialQuestions())
                        database?.learningTopicDao()?.insertTopics(DatabaseInitializer.getInitialLearningTopics())
                        database?.adminDao()?.insertAdmin(
                            com.akash.gkgsmaster.data.model.AdminUser("akashray398", "Akash@3980")
                        )
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
        return database
    }

    @Provides
    fun provideQuestionDao(database: AppDatabase): QuestionDao {
        return database.questionDao()
    }

    @Provides
    fun provideCurrentAffairDao(database: AppDatabase): CurrentAffairDao {
        return database.currentAffairDao()
    }

    @Provides
    fun provideBookletDao(database: AppDatabase): BookletDao {
        return database.bookletDao()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    fun provideBookmarkDao(database: AppDatabase): BookmarkDao {
        return database.bookmarkDao()
    }

    @Provides
    fun provideLearningTopicDao(database: AppDatabase): LearningTopicDao {
        return database.learningTopicDao()
    }

    @Provides
    fun provideRecentActivityDao(database: AppDatabase): RecentActivityDao {
        return database.recentActivityDao()
    }

    @Provides
    fun provideAdminDao(database: AppDatabase): AdminDao {
        return database.adminDao()
    }

    @Provides
    fun provideQuizHistoryDao(database: AppDatabase): QuizHistoryDao {
        return database.quizHistoryDao()
    }
}
