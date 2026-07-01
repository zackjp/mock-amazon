package com.zackjp.mockamazon.feature.home

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zackjp.mockamazon.feature.home.view.HeroCarousel
import com.zackjp.mockamazon.feature.home.view.IntentCarousel
import com.zackjp.mockamazon.shared.ignoreParentPadding
import com.zackjp.mockamazon.shared.ui.screen.ErrorScreen
import com.zackjp.mockamazon.shared.ui.screen.LoadingScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import com.zackjp.mockamazon.shared.R as SharedR

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    onViewProduct: (Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val screenState: HomeScreenState by viewModel.screenState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        innerPadding = innerPadding,
        onViewProduct = onViewProduct,
        screenState = screenState,
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    onViewProduct: (Int) -> Unit = {},
    screenState: HomeScreenState,
) {
    when (screenState) {
        is HomeScreenState.Loading -> LoadingScreen(
            modifier = modifier, // pass modifier without innerPadding to cover full screen, including gradient
        )

        is HomeScreenState.Loaded -> LoadedView(
            innerPadding = innerPadding,
            modifier = modifier,
            onViewProduct = onViewProduct,
            screenState = screenState,
        )

        is HomeScreenState.Error -> ErrorScreen(modifier = modifier.padding(innerPadding))
    }
}

@Composable
private fun LoadedView(
    innerPadding: PaddingValues,
    modifier: Modifier,
    onViewProduct: (Int) -> Unit,
    screenState: HomeScreenState.Loaded,
) {
    val paddingXLarge = dimensionResource(SharedR.dimen.padding_xlarge)
    val mainContentPadding = dimensionResource(SharedR.dimen.main_content_padding_horizontal)

    val currentLayoutDirection = LocalLayoutDirection.current

    val coroutineScope = rememberCoroutineScope()
    val homeUxState: HomeUxState = rememberHomeUxState()

    LazyColumn(
        modifier = modifier
            .graphicsLayer {
                alpha = homeUxState.contentAlpha
            }
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
                val topPadding =
                    innerPadding.calculateTopPadding() + dimensionResource(SharedR.dimen.padding_medium)

                Box(
                    modifier = Modifier
                        .drawWithCache {
                            val topPaddingPx = topPadding.toPx()
                            val calculatedCarouselHeightPx = size.height - topPaddingPx
                            val endGradientHeightPx =
                                topPaddingPx + calculatedCarouselHeightPx * 0.8f
                            val verticalGradient = Brush.verticalGradient(
                                colors = listOf(homeUxState.topGradientColor, Color.Transparent),
                                endY = endGradientHeightPx
                            )
                            onDrawBehind {
                                drawRect(
                                    brush = verticalGradient,
                                )
                            }
                        }
                        .ignoreParentPadding(mainContentPadding)
                        .matchParentSize()
                )

                Column {
                    Spacer(modifier = Modifier.height(topPadding))

                    HeroCarousel(
                        mainContentHorizontalPadding = mainContentPadding,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onColorChanged = { color: Color ->
                            coroutineScope.launch { homeUxState.animateGradientTo(color) }
                        },
                        onViewProduct = onViewProduct,
                        heroCarouselCards = screenState.heroCarouselCards,
                    )
                }
            }

            Spacer(modifier = Modifier.height(paddingXLarge))
        }

        items(screenState.intentCarousels) { intentCarousel ->
            IntentCarousel(
                intentCarousel = intentCarousel,
                mainContentHorizontalPadding = mainContentPadding,
                modifier = Modifier.fillMaxWidth(),
                onViewProduct = onViewProduct,
            )

            Spacer(modifier = Modifier.height(paddingXLarge))
        }

    }
}

@Composable
private fun rememberHomeUxState(): HomeUxState {
    return rememberSaveable(saver = HomeUxStateSaver) {
        HomeUxState(
            initialTopColor = Color.Transparent,
            initialIsFirstLoad = true,
        )
    }
}

private val HomeUxStateSaver = mapSaver(
    save = {
        mapOf(
            "top_color" to it.topGradientColor.toArgb(),
            "is_first_load" to it.isFirstLoad,
        )
    },
    restore = { savedMap ->
        HomeUxState(
            initialTopColor = Color(savedMap["top_color"] as Int),
            initialIsFirstLoad = savedMap["is_first_load"] as Boolean,
        )
    }
)

private class HomeUxState(
    initialTopColor: Color,
    initialIsFirstLoad: Boolean,
) {

    /** Init with topColor to prevent gradient re-fading on every config change */
    private val _topColor = Animatable(initialTopColor)

    /** Init with isFirstLoad to prevent pop-in on config change */
    private val _contentAlpha = Animatable(if (initialIsFirstLoad) 0f else 1f)

    var isFirstLoad = initialIsFirstLoad
        private set

    val topGradientColor: Color
        get() = _topColor.value

    val contentAlpha: Float
        get() = _contentAlpha.value

    suspend fun animateGradientTo(targetColor: Color) {
        if (targetColor == Color.Transparent) return

        coroutineScope {
            if (isFirstLoad) {
                isFirstLoad = false
                launch { _contentAlpha.animateTo(1f, spring()) }
                launch { _topColor.snapTo(targetColor) }
            } else {
                launch { _contentAlpha.snapTo(1f) }
                launch {
                    _topColor.animateTo(
                        targetColor,
                        tween(durationMillis = 300, easing = LinearEasing),
                    )
                }
            }
        }
    }
}
