package com.example.fakeamazon.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.base.DispatcherProvider
import com.example.fakeamazon.data.DealsRepository
import com.example.fakeamazon.model.RecommendationGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _recommendationGroups: MutableStateFlow<List<RecommendationGroup>> =
        MutableStateFlow(emptyList())
    val recommendationGroups = _recommendationGroups.asStateFlow()

    fun load() {
        viewModelScope.launch(dispatcherProvider.default) {
            delay(1500L)
            _recommendationGroups.value = DealsRepository.RECOMMENDED_DEALS
        }
    }

}
