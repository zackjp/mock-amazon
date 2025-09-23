package com.example.fakeamazon.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fakeamazon.R
import com.example.fakeamazon.base.ignoreParentPadding
import com.example.fakeamazon.features.home.view.RecommendedDealsSection
import com.example.fakeamazon.features.home.view.TopHomeSection

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    val topHomeGroups by viewModel.topHomeGroups.collectAsStateWithLifecycle()
    val recommendationGroups by viewModel.recommendationGroups.collectAsStateWithLifecycle()

    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val mainContentPadding = paddingLarge

    val currentLayoutDirection = LocalLayoutDirection.current

    Column(
        modifier = modifier
            .padding(
                top = 0.dp,
                start = innerPadding.calculateStartPadding(currentLayoutDirection),
                end = innerPadding.calculateEndPadding(currentLayoutDirection),
                bottom = innerPadding.calculateBottomPadding()
            )
            .padding(horizontal = mainContentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        val localDensity = LocalDensity.current
        var topHomeHeightPx by remember { mutableIntStateOf(0) }
        var topHomeColor by remember { mutableStateOf(Color.Transparent) }
        val endGradientHeight = with(localDensity) { (topHomeHeightPx * .8).toInt().toDp() }

        Box {
            Box(
                modifier = Modifier
                    .background(Brush.verticalGradient(listOf(topHomeColor, Color.Transparent)))
                    .ignoreParentPadding(mainContentPadding)
                    .fillMaxWidth()
                    .height(innerPadding.calculateTopPadding() + endGradientHeight)
            )

            Column {
                Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))

                TopHomeSection(
                    onColorChanged = { color: Color -> topHomeColor = color },
                    mainContentHorizontalPadding = mainContentPadding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { topHomeHeightPx = it.height },
                    topHomeGroups = topHomeGroups,
                )
            }
        }

        Spacer(modifier = Modifier.height(paddingLarge))

        RecommendedDealsSection(
            mainContentHorizontalPadding = mainContentPadding,
            modifier = Modifier.fillMaxWidth(),
            recommendationGroups = recommendationGroups
        )

        Spacer(modifier = Modifier.height(paddingLarge))
    }
}
