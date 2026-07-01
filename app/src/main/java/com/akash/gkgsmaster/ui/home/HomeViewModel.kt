package com.akash.gkgsmaster.ui.home

import androidx.lifecycle.*
import com.akash.gkgsmaster.data.PreferenceManager
import com.akash.gkgsmaster.data.model.*
import com.akash.gkgsmaster.data.repository.CurrentAffairRepository
import com.akash.gkgsmaster.data.repository.HomeRepository
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val currentAffairRepository: CurrentAffairRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val userName: LiveData<String> = preferenceManager.userName
        .map { it ?: "Aspirant" }
        .asLiveData()

    val dailyQuote: LiveData<String> = repository.getDailyQuote().asLiveData()
    
    val currentAffairs: LiveData<Resource<List<CurrentAffairEntity>>> = 
        currentAffairRepository.getCurrentAffairs().asLiveData()

    val categories: LiveData<List<HomeCategory>> = repository.getCategories().asLiveData()
    val userProgress: LiveData<UserProgress> = repository.getUserProgress().asLiveData()
    val recentActivity: LiveData<List<RecentActivity>> = repository.getRecentActivity().asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        refreshDashboard()
    }

    fun refreshDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            // Additional refreshes if needed
            _isLoading.value = false
        }
    }
}
