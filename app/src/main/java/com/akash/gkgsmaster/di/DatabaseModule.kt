package com.akash.gkgsmaster.di

import android.content.Context
import androidx.room.Room
import com.akash.gkgsmaster.data.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "gk_gs_master_db"
        )
            .fallbackToDestructiveMigration()
            .build()
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
}
