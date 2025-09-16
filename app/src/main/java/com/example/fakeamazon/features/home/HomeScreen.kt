package com.example.fakeamazon.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fakeamazon.R
import com.example.fakeamazon.features.home.view.RecommendedDealsSection
import com.example.fakeamazon.features.home.view.TopHomeSection

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

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val paddingMedium = dimensionResource(R.dimen.padding_medium)

        TopHomeSection(modifier = Modifier.fillMaxWidth().padding(horizontal = paddingMedium))

        Spacer(modifier = Modifier.height(paddingMedium))

        RecommendedDealsSection(
            modifier = Modifier.padding(horizontal = paddingMedium),
            recommendationGroups = recommendationGroups
        )
    }
}
