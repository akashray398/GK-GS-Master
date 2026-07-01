package com.akash.gkgsmaster.ui.booklet

import androidx.lifecycle.*
import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.data.model.BookletPage
import com.akash.gkgsmaster.data.repository.BookletRepository
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookletViewModel @Inject constructor(
    private val repository: BookletRepository
) : ViewModel() {

    private val _booklets = MutableLiveData<Resource<List<Booklet>>>()
    val booklets: LiveData<Resource<List<Booklet>>> = _booklets

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> = _currentPage

    init {
        loadBooklets()
    }

    fun loadBooklets() {
        viewModelScope.launch {
            repository.getBooklets()
                .onStart { _booklets.value = Resource.Loading() }
                .catch { _booklets.value = Resource.Error(it.message ?: "Error") }
                .collect { _booklets.value = Resource.Success(it) }
        }
    }

    fun updatePage(page: Int) {
        _currentPage.value = page
    }
}
