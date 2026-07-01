package com.akash.gkgsmaster.ui.science

import androidx.lifecycle.*
import com.akash.gkgsmaster.data.model.ScienceCategory
import com.akash.gkgsmaster.data.model.ScienceTopic
import com.akash.gkgsmaster.data.repository.ScienceRepository
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScienceViewModel @Inject constructor(
    private val repository: ScienceRepository
) : ViewModel() {

    private val _categories = MutableLiveData<Resource<List<ScienceCategory>>>()
    val categories: LiveData<Resource<List<ScienceCategory>>> = _categories

    private val allCategories = mutableListOf<ScienceCategory>()

    private val _topics = MutableLiveData<Resource<List<ScienceTopic>>>()
    val topics: LiveData<Resource<List<ScienceTopic>>> = _topics

    private val allTopics = mutableListOf<ScienceTopic>()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onStart { _categories.value = Resource.Loading() }
                .catch { _categories.value = Resource.Error(it.message ?: "Error") }
                .collect { 
                    allCategories.clear()
                    allCategories.addAll(it)
                    _categories.value = Resource.Success(it) 
                }
        }
    }

    fun searchCategories(query: String) {
        val filtered = if (query.isEmpty()) allCategories 
        else allCategories.filter { it.name.contains(query, ignoreCase = true) }
        _categories.value = Resource.Success(filtered)
    }

    fun loadTopics(categoryId: String) {
        viewModelScope.launch {
            repository.getTopics(categoryId)
                .onStart { _topics.value = Resource.Loading() }
                .catch { _topics.value = Resource.Error(it.message ?: "Error") }
                .collect { 
                    allTopics.clear()
                    allTopics.addAll(it)
                    _topics.value = Resource.Success(it) 
                }
        }
    }

    fun searchTopics(query: String) {
        val filtered = if (query.isEmpty()) allTopics
        else allTopics.filter { it.title.contains(query, ignoreCase = true) }
        _topics.value = Resource.Success(filtered)
    }
}
