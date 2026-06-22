package com.zackjp.mockamazon.app.ui.view

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zackjp.mockamazon.feature.search.SearchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalSearchBarViewModel @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
) : ViewModel() {

    private val _searchText = MutableStateFlow(TextFieldValue("", TextRange.Companion.Zero))
    val searchText = _searchText.asStateFlow()

    fun updateSearchText(text: TextFieldValue) {
        _searchText.value = text
    }

    fun saveQuery(query: String) {
        viewModelScope.launch {
            searchHistoryRepository.saveQuery(query)
        }
    }

}
