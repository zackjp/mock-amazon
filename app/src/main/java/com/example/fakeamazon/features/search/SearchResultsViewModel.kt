package com.example.fakeamazon.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.SearchApiDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val searchApiDataSource: SearchApiDataSource
) : ViewModel() {

    private val _screenState =
        MutableStateFlow<SearchResultsScreenState>(SearchResultsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    fun load(searchString: String) {
        viewModelScope.launch {
            try {
                val searchResults = searchApiDataSource.getSearchResults(searchString)
                _screenState.value = SearchResultsScreenState.Loaded(searchResults)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _screenState.value = SearchResultsScreenState.Error
            }
        }
    }

}
