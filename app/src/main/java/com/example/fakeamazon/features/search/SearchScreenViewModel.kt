package com.example.fakeamazon.features.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchScreenViewModel @Inject constructor() : ViewModel() {

    val searchItems = listOf("mixed nuts", "popcorn", "snacks", "sneakers", "household")

}