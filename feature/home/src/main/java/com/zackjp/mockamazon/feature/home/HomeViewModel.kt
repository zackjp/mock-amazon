package com.zackjp.mockamazon.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zackjp.mockamazon.shared.data.HomeRepository
import com.zackjp.mockamazon.shared.model.ItemSection
import com.zackjp.mockamazon.shared.model.TopHomeGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val screenState = _screenState
        .onStart { load() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeScreenState.Loading
        )

    private fun load() {
        viewModelScope.launch {
            _screenState.value = HomeScreenState.Loading

            try {
                val topHomeDeferred: Deferred<List<TopHomeGroup>> = viewModelScope.async {
                    homeRepository.getTopHomeGroups()
                }

                val homeSectionsDeferred: Deferred<List<ItemSection>> = viewModelScope.async {
                    homeRepository.getHomeSections()
                }

                awaitAll(topHomeDeferred, homeSectionsDeferred)

                _screenState.value = HomeScreenState.Loaded(
                    homeSections = homeSectionsDeferred.await(),
                    topHomeGroups = topHomeDeferred.await(),
                )
            } catch(e: Exception) {
                if (e is CancellationException) throw e
                _screenState.value = HomeScreenState.Error
            }
        }
    }

}
