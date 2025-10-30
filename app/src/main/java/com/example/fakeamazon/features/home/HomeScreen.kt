package com.example.fakeamazon.features.home

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.fakeamazon.shared.ignoreParentPadding
import com.example.fakeamazon.features.home.view.HomeSectionView
import com.example.fakeamazon.features.home.view.TopHomeSection

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    onViewProduct: (Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    val topHomeGroups by viewModel.topHomeGroups.collectAsStateWithLifecycle()
    val homeSections by viewModel.homeSections.collectAsStateWithLifecycle()

    val paddingXLarge = dimensionResource(R.dimen.padding_xlarge)
    val mainContentPadding = dimensionResource(R.dimen.main_content_padding_horizontal)

    val currentLayoutDirection = LocalLayoutDirection.current

    val localDensity = LocalDensity.current
    var topHomeHeightPx by remember { mutableIntStateOf(0) }
    val endGradientHeight = with(localDensity) { (topHomeHeightPx * .8).toInt().toDp() }
    var targetTopColor by remember { mutableStateOf(Color.Transparent) }
    val colorTransition = updateTransition(targetState = targetTopColor)
    val currentTopColor by colorTransition.animateColor(transitionSpec = {
        tween(
            durationMillis = 300,
            easing = LinearEasing
        )
    }) { it }

    LazyColumn(
        modifier = modifier
            .padding(
                top = 0.dp,
                start = innerPadding.calculateStartPadding(currentLayoutDirection),
                end = innerPadding.calculateEndPadding(currentLayoutDirection),
                bottom = innerPadding.calculateBottomPadding()
            )
            .padding(horizontal = mainContentPadding)
    ) {
        item {
            Box {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(listOf(currentTopColor, Color.Transparent))
                        )
                        .ignoreParentPadding(mainContentPadding)
                        .fillMaxWidth()
                        .height(innerPadding.calculateTopPadding() + endGradientHeight)
                )

                Column {
                    Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                    TopHomeSection(
                        mainContentHorizontalPadding = mainContentPadding,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onSizeChanged { topHomeHeightPx = it.height },
                        onColorChanged = { color: Color -> targetTopColor = color },
                        onViewProduct = onViewProduct,
                        topHomeGroups = topHomeGroups,
                    )
                }
            }

            Spacer(modifier = Modifier.height(paddingXLarge))
        }

        items(homeSections) { homeSection ->
            HomeSectionView(
                itemSection = homeSection,
                mainContentHorizontalPadding = mainContentPadding,
                modifier = Modifier.fillMaxWidth(),
                onViewProduct = onViewProduct,
            )

            Spacer(modifier = Modifier.height(paddingXLarge))
        }

    }
}
