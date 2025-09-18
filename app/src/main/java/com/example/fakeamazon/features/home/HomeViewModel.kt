package com.example.fakeamazon.features.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.R
import com.example.fakeamazon.data.DealsRepository
import com.example.fakeamazon.features.home.model.DisplayableItem
import com.example.fakeamazon.features.home.model.TopHomeGroup
import com.example.fakeamazon.model.RecommendationGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dealsRepository: DealsRepository,
) : ViewModel() {

    private val mockTopHomeGroups: List<TopHomeGroup> = listOf(
        TopHomeGroup(
            "More top\npicks for you",
            Color(0xFF3A6DB1),
            listOf(
                DisplayableItem(imageId = R.drawable.item_headphones),
                DisplayableItem(imageId = R.drawable.item_backpack),
                DisplayableItem(imageId = R.drawable.item_matcha),
                DisplayableItem(imageId = R.drawable.item_handsoap),
                DisplayableItem(imageId = R.drawable.item_detergent),
            )
        ),
        TopHomeGroup(
            "Kitchen\ncorner",
            Color(0xFF6AD17D),
            listOf(
                DisplayableItem(imageId = R.drawable.item_kitchen_sponge),
                DisplayableItem(imageId = R.drawable.item_matcha),
                DisplayableItem(imageId = R.drawable.item_sandwich_bags),
                DisplayableItem(imageId = R.drawable.item_dishwash_detergent),
                DisplayableItem(imageId = R.drawable.item_handsoap),
            )
        ),
        TopHomeGroup(
            "Amazon picks\nfor you",
            Color(0xFFED7571),
            listOf(
                DisplayableItem(imageId = R.drawable.item_game_monopoly_deal),
                DisplayableItem(imageId = R.drawable.item_game_catan),
                DisplayableItem(imageId = R.drawable.item_game_ra),
                DisplayableItem(imageId = R.drawable.item_game_lost_cities),
                DisplayableItem(imageId = R.drawable.item_game_forest_shuffle),
            )
        )
    )

    private val _topHomeGroups = MutableStateFlow<List<TopHomeGroup>>(emptyList())
    val topHomeGroups = _topHomeGroups.asStateFlow()

    private val _recommendationGroups: MutableStateFlow<List<RecommendationGroup>> =
        MutableStateFlow(emptyList())
    val recommendationGroups = _recommendationGroups.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _topHomeGroups.value = mockTopHomeGroups
            _recommendationGroups.value = dealsRepository.loadRecommendedDeals()
        }
    }

}
