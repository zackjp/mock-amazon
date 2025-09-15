package com.example.fakeamazon.features.home

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

val DISCOUNT_RED: Color = Color(0xFFC60B37)
val RECOMMENDED_CARD_BORDER_COLOR: Color = Color(0xFFD0D4D4)
val RECOMMENDED_ITEM_BG_COLOR: Color = Color(0xFFF7F7F7)

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    val recommendationGroups by viewModel.recommendationGroups.collectAsStateWithLifecycle()

    RecommendedDealsSection(
        modifier = modifier.padding(horizontal = 16.dp),
        recommendationGroups = recommendationGroups
    )
}
