package com.zackjp.mockamazon.feature.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zackjp.mockamazon.core.model.HeroCarouselCard
import com.zackjp.mockamazon.feature.home.R
import com.zackjp.mockamazon.feature.home.component.ItemDisplayWindow
import com.zackjp.mockamazon.shared.ignoreParentPadding
import kotlin.math.abs
import com.zackjp.mockamazon.shared.R as SharedR

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HeroCarousel(
    mainContentHorizontalPadding: Dp,
    modifier: Modifier = Modifier,
    onColorChanged: (Color) -> Unit = {},
    onViewProduct: (Int) -> Unit = {},
    heroCarouselCards: List<HeroCarouselCard>,
) {
    val cardWidth = dimensionResource(R.dimen.home_hero_card_width)
    val cardAspectRatio =
        ResourcesCompat.getFloat(LocalResources.current, R.dimen.home_hero_card_aspect_ratio)
    val paddingSmall = dimensionResource(SharedR.dimen.padding_small)

    val lazyListState = rememberLazyListState()
    val heroCards by rememberUpdatedState(heroCarouselCards)

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            val closestItemToCenter = visibleItems.minByOrNull { item ->
                abs(viewportCenter - (item.offset + item.size / 2))
            }

            closestItemToCenter?.index?.let {
                heroCards[it].background
            } ?: Color.Transparent
        }.collect { color ->
            onColorChanged(color)
        }
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = mainContentHorizontalPadding),
        modifier = modifier.ignoreParentPadding(mainContentHorizontalPadding),
        state = lazyListState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState),
        horizontalArrangement = Arrangement.spacedBy(paddingSmall)
    ) {
        items(items = heroCards, key = { it.heroId }) { heroCard ->
            HeroCard(
                modifier = Modifier
                    .width(cardWidth)
                    .aspectRatio(cardAspectRatio),
                onViewProduct = onViewProduct,
                heroCarouselCard = heroCard,
            )
        }
    }
}

@Composable
private fun HeroCard(
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit = {},
    heroCarouselCard: HeroCarouselCard,
) {
    val paddingMedium = dimensionResource(SharedR.dimen.padding_medium)
    val itemSpacing = dimensionResource(SharedR.dimen.padding_small)
    val cardPadding = paddingMedium

    val cardBackground = heroCarouselCard.background
    val cardBackgroundImageId = heroCarouselCard.backgroundImageId
    val productGridHeightFraction: Float = heroCarouselCard.productGridHeightFraction
    val titleForeground = remember(cardBackground) {
        getContrastColor(cardBackground)
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            HeroBackground(
                modifier = Modifier
                    .background(cardBackground)
                    .fillMaxSize(),
                imageId = cardBackgroundImageId,
            )

            Column(
                modifier = Modifier
                    .padding(cardPadding)
                    .fillMaxSize(),
            ) {
                UpperHeroSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1 - productGridHeightFraction),
                    preTitle = heroCarouselCard.preTitle,
                    title = heroCarouselCard.title,
                    titleForeground = titleForeground,
                )

                ItemDisplayWindow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    productTiles = heroCarouselCard.productTiles,
                    displayDiscounts = false,
                    itemSpacing = itemSpacing,
                    onViewProduct = onViewProduct,
                )
            }
        }
    }
}

@Composable
fun HeroBackground(
    modifier: Modifier = Modifier,
    imageId: Int?,
) {
    val context = LocalContext.current
    val model = remember(imageId, context) {
        imageId?.let {
            ImageRequest.Builder(context)
                .data(it)
                .crossfade(true)
                .build()
        }
    }

    AsyncImage(
        modifier = modifier,
        contentDescription = null,
        model = model,
    )
}

@Composable
private fun UpperHeroSection(
    modifier: Modifier = Modifier,
    preTitle: String?,
    title: String,
    titleForeground: Color,
) {
    Column(
        modifier = modifier,
    ) {
        if (preTitle != null) {
            Text(
                color = titleForeground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = preTitle,
                style = MaterialTheme.typography.labelMedium,
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            color = titleForeground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = title,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

private fun getContrastColor(color: Color): Color {
    return if (ColorUtils.calculateLuminance(color.toArgb()) > 0.3) {
        Color.Black
    } else {
        Color.White
    }
}
