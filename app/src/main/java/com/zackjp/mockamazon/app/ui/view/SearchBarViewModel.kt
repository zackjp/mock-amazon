package com.zackjp.mockamazon.app.ui.view

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchBarViewModel @Inject constructor() : ViewModel() {

    private val _searchText = MutableStateFlow(TextFieldValue("", TextRange.Companion.Zero))
    val searchText = _searchText.asStateFlow()

    fun updateSearchText(text: TextFieldValue) {
        _searchText.value = text
    }

}
