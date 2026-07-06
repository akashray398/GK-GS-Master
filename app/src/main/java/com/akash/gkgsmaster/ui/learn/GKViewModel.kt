package com.akash.gkgsmaster.ui.learn

import androidx.lifecycle.*
import com.akash.gkgsmaster.data.model.GKArticle
import com.akash.gkgsmaster.data.model.GKCategory
import com.akash.gkgsmaster.data.model.LearningTopicEntity
import com.akash.gkgsmaster.data.repository.GKRepository
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GKViewModel @Inject constructor(
    private val repository: GKRepository
) : ViewModel() {

    private val _categories = MutableLiveData<Resource<List<GKCategory>>>()
    val categories: LiveData<Resource<List<GKCategory>>> = _categories

    private val allCategories = mutableListOf<GKCategory>()

    private val _articles = MutableLiveData<Resource<List<GKArticle>>>()
    val articles: LiveData<Resource<List<GKArticle>>> = _articles

    private val allArticles = mutableListOf<GKArticle>()

    private val _currentTopic = MutableLiveData<LearningTopicEntity?>()
    val currentTopic: LiveData<LearningTopicEntity?> = _currentTopic

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onStart { _categories.value = Resource.Loading() }
                .catch { _categories.value = Resource.Error(it.message ?: "Error loading categories") }
                .collect { 
                    allCategories.clear()
                    allCategories.addAll(it)
                    _categories.value = Resource.Success(it) 
                }
        }
    }

    fun searchCategories(query: String) {
        val filtered = if (query.isEmpty()) {
            allCategories
        } else {
            allCategories.filter { it.name.contains(query, ignoreCase = true) }
        }
        println("Found ${filtered.size} categories for query: $query")
        _categories.value = Resource.Success(filtered)
    }

    fun loadArticles(categoryId: String) {
        viewModelScope.launch {
            println("Loading articles for $categoryId")
            repository.getArticles(categoryId)
                .onStart { _articles.value = Resource.Loading() }
                .catch { _articles.value = Resource.Error(it.message ?: "Error loading articles") }
                .collect { 
                    allArticles.clear()
                    allArticles.addAll(it)
                    _articles.value = Resource.Success(it) 
                }
        }
    }

    fun searchArticles(query: String) {
        val filtered = if (query.isEmpty()) {
            allArticles
        } else {
            allArticles.filter { it.title.contains(query, ignoreCase = true) }
        }
        _articles.value = Resource.Success(filtered)
    }

    fun loadTopicById(id: String) {
        viewModelScope.launch {
            val topic = repository.getTopicById(id)
            _currentTopic.value = topic
            println("Loaded topic: ${topic?.title}")
        }
    }

    fun toggleBookmark(id: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleTopicBookmark(id, isBookmarked)
            println("GKViewModel: Toggled bookmark for $id to $isBookmarked")
            val topic = _currentTopic.value
            if (topic?.id == id) {
                _currentTopic.value = topic.copy(isBookmarked = isBookmarked)
            }
        }
    }
}
