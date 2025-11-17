package com.example.fakeamazon.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.SearchApiDataSource
import com.example.fakeamazon.shared.model.ProductInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val searchApiDataSource: SearchApiDataSource
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<ProductInfo>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun load(searchString: String) {
        viewModelScope.launch {
            _searchResults.value = searchApiDataSource.getHardcodedSearchResults(searchString)
        }
    }

}
