package com.akash.gkgsmaster.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akash.gkgsmaster.data.model.SearchResult
import com.akash.gkgsmaster.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gkRepository: GKRepository,
    private val scienceRepository: ScienceRepository,
    private val bookletRepository: BookletRepository,
    private val currentAffairRepository: CurrentAffairRepository,
    private val questionRepository: QuestionRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private var searchJob: Job? = null

    fun search(query: String) {
        searchJob?.cancel()
        if (query.length < 2) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            
            val results = mutableListOf<SearchResult>()
            
            // GK Search
            gkRepository.searchArticles(query).forEach {
                results.add(SearchResult.GKArticleResult(it.id, it.title, it.category))
            }
            
            // Science Search
            scienceRepository.searchTopics(query).forEach {
                results.add(SearchResult.ScienceTopicResult(it.id, it.title, it.category))
            }
            
            // Booklets Search
            bookletRepository.searchBooklets(query).forEach {
                results.add(SearchResult.BookletResult(it.id, it.title, it.author))
            }
            
            // Affairs Search
            currentAffairRepository.searchAffairs(query).forEach {
                results.add(SearchResult.CurrentAffairResult(it.id, it.title, it.date))
            }
            
            // Articles Search
            gkRepository.searchTopics(query).forEach {
                results.add(SearchResult.GKArticleResult(it.id, it.title, it.category))
            }

            // Booklets Search
            bookletRepository.getBooklets().first().filter {
                it.title.contains(query, true) || it.author.contains(query, true)
            }.forEach {
                results.add(SearchResult.BookletResult(it.id, it.title, it.author))
            }
            
            // Questions Search
            questionRepository.searchQuestions(query).forEach {
                results.add(SearchResult.QuestionResult(it.id, it.text, it.category))
            }

            // Notes Search
            noteRepository.getAllNotes().first().filter { 
                it.title.contains(query, true) || it.content.contains(query, true)
            }.forEach {
                results.add(SearchResult.NoteResult(it.id, it.title))
            }
            
            _searchResults.value = results
        }
    }
}
