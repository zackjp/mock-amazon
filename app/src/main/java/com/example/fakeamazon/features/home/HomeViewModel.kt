package com.example.fakeamazon.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.HomeRepository
import com.example.fakeamazon.shared.model.ItemSection
import com.example.fakeamazon.shared.model.TopHomeGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _topHomeGroups = MutableStateFlow<List<TopHomeGroup>>(emptyList())
    val topHomeGroups = _topHomeGroups.asStateFlow()

    private val _itemSections: MutableStateFlow<List<ItemSection>> =
        MutableStateFlow(emptyList())
    val itemSections = _itemSections.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _topHomeGroups.value = homeRepository.loadTopHome()
            _itemSections.value = homeRepository.loadSections()
        }
    }

}
