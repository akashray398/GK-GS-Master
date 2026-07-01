package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.ScienceCategory
import com.akash.gkgsmaster.data.model.ScienceTable
import com.akash.gkgsmaster.data.model.ScienceTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScienceRepository @Inject constructor() {

    fun getCategories(): Flow<List<ScienceCategory>> = flow {
        emit(listOf(
            ScienceCategory("1", "Physics", R.drawable.ic_placeholder, R.color.primary),
            ScienceCategory("2", "Chemistry", R.drawable.ic_placeholder, R.color.secondary),
            ScienceCategory("3", "Biology", R.drawable.ic_placeholder, R.color.accent),
            ScienceCategory("4", "Human Body", R.drawable.ic_placeholder, R.color.glow),
            ScienceCategory("5", "Environment", R.drawable.ic_placeholder, R.color.primary),
            ScienceCategory("6", "Space", R.drawable.ic_placeholder, R.color.secondary),
            ScienceCategory("7", "Scientific Names", R.drawable.ic_placeholder, R.color.accent),
            ScienceCategory("8", "Inventions", R.drawable.ic_placeholder, R.color.glow)
        ))
    }

    fun getTopics(categoryId: String): Flow<List<ScienceTopic>> = flow {
        // Placeholder data
        emit(listOf(
            ScienceTopic(
                id = "1",
                title = "Laws of Motion",
                content = "Newton's laws of motion are three physical laws that, together, laid the foundation for classical mechanics.",
                category = "Physics",
                importantPoints = listOf(
                    "First Law: An object at rest remains at rest unless acted upon by a force.",
                    "Second Law: F = ma",
                    "Third Law: For every action, there is an equal and opposite reaction."
                ),
                examples = listOf(
                    "A ball rolling on the ground eventually stops due to friction (First Law).",
                    "Pushing an empty cart is easier than pushing a loaded one (Second Law)."
                ),
                tables = listOf(
                    ScienceTable(
                        title = "Motion Equations",
                        headers = listOf("Equation", "Description"),
                        rows = listOf(
                            listOf("v = u + at", "Final velocity"),
                            listOf("s = ut + ½at²", "Displacement")
                        )
                    )
                )
            )
        ))
    }

    suspend fun searchTopics(query: String): List<ScienceTopic> {
        // This would normally be a database search
        val allTopics = listOf(
            ScienceTopic(id = "1", title = "Laws of Motion", content = "Newton's laws...", category = "Physics"),
            ScienceTopic(id = "2", title = "Atomic Structure", content = "Elements and atoms...", category = "Chemistry"),
            ScienceTopic(id = "3", title = "Human Circulatory System", content = "Heart and blood...", category = "Human Body")
        )
        return if (query.isEmpty()) emptyList() else allTopics.filter {
            it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
        }
    }
}
