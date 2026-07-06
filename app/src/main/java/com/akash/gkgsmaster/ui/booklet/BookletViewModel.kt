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
                .collect { list ->
                    // Auto-populate with new books if they are missing
                    val initialList = com.akash.gkgsmaster.data.model.BookletDataGenerator.getInitialBooklets()
                    val missing = initialList.filter { initial -> list.none { it.id == initial.id } }
                    
                    if (missing.isNotEmpty()) {
                        println("BookletViewModel: Found ${missing.size} missing booklets, populating...")
                        missing.forEach { repository.insertBooklet(it) }
                    } else {
                        _booklets.value = Resource.Success(list)
                    }
                }
        }
    }

    fun updatePage(page: Int) {
        println("Updating page to $page")
        _currentPage.value = page
    }

    fun saveProgress(bookletId: String, pageNumber: Int) {
        viewModelScope.launch {
            repository.saveProgress(bookletId, pageNumber)
        }
    }

    fun insertBooklet(booklet: Booklet) {
        viewModelScope.launch {
            repository.insertBooklet(booklet)
            println("Inserted booklet: ${booklet.title}")
            loadBooklets() // Refresh list
        }
    }
}
