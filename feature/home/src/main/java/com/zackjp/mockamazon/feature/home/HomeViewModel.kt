package com.zackjp.mockamazon.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zackjp.mockamazon.shared.data.HomeRepository
import com.zackjp.mockamazon.shared.ui.model.CategoryCarousel
import com.zackjp.mockamazon.shared.ui.model.HeroCarouselCard
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
                val heroCardsDeferred: Deferred<List<HeroCarouselCard>> = viewModelScope.async {
                    homeRepository.getHeroCarouselCards()
                }

                val categoryCarouselsDeferred: Deferred<List<CategoryCarousel>> = viewModelScope.async {
                    homeRepository.getCategoryCarousels()
                }

                awaitAll(heroCardsDeferred, categoryCarouselsDeferred)

                _screenState.value = HomeScreenState.Loaded(
                    categoryCarousels = categoryCarouselsDeferred.await(),
                    heroCarouselCards = heroCardsDeferred.await(),
                )
            } catch(e: Exception) {
                if (e is CancellationException) throw e
                _screenState.value = HomeScreenState.Error
            }
        }
    }

}
