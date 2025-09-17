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
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fakeamazon.R
import com.example.fakeamazon.features.home.view.RecommendedDealsSection
import com.example.fakeamazon.features.home.view.TopHomeSection

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    val topHomeGroups by viewModel.topHomeGroups.collectAsStateWithLifecycle()
    val recommendationGroups by viewModel.recommendationGroups.collectAsStateWithLifecycle()

    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val mainContentPadding = paddingLarge
    Column(
        modifier = modifier
            .padding(start = mainContentPadding)
            .verticalScroll(rememberScrollState())
    ) {

        TopHomeSection(
            mainContentHorizontalPadding = mainContentPadding,
            modifier = Modifier.fillMaxWidth(),
            topHomeGroups = topHomeGroups,
        )

        Spacer(modifier = Modifier.height(paddingLarge))

        RecommendedDealsSection(
            mainContentHorizontalPadding = mainContentPadding,
            modifier = Modifier.fillMaxWidth(),
            recommendationGroups = recommendationGroups
        )
    }
}
