package com.akash.gkgsmaster.ui.affairs

import androidx.lifecycle.*
import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import com.akash.gkgsmaster.data.repository.CurrentAffairRepository
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentAffairsViewModel @Inject constructor(
    private val repository: CurrentAffairRepository
) : ViewModel() {

    private val _currentAffairs = MutableLiveData<Resource<List<CurrentAffairEntity>>>()
    val currentAffairs: LiveData<Resource<List<CurrentAffairEntity>>> = _currentAffairs

    private var currentCategory: String? = null

    fun loadCurrentAffairs(category: String? = null, forceRefresh: Boolean = false) {
        currentCategory = category
        viewModelScope.launch {
            repository.getCurrentAffairs(category).collect {
                _currentAffairs.value = it
            }
        }
    }

    fun refresh() {
        loadCurrentAffairs(currentCategory, true)
    }

    fun toggleBookmark(id: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(id, isBookmarked)
        }
    }
}
