package com.example.fakeamazon.features.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.fakeamazon.R
import com.example.fakeamazon.base.ignoreParentPadding
import com.example.fakeamazon.features.home.component.ItemDisplayWindow
import com.example.fakeamazon.model.TopHomeGroup
import kotlin.math.abs

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopHomeSection(
    mainContentHorizontalPadding: Dp,
    modifier: Modifier = Modifier,
    onColorChanged: (Color) -> Unit = {},
    onViewProduct: () -> Unit = {},
    topHomeGroups: List<TopHomeGroup>,
) {
    val cardWidth = dimensionResource(R.dimen.top_home_card_width)
    val cardHeight = dimensionResource(R.dimen.top_home_card_height)
    val paddingSmall = dimensionResource(R.dimen.padding_small)

    val lazyListState = rememberLazyListState()
    val groups by rememberUpdatedState(topHomeGroups)

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            val closestItemToCenter = visibleItems.minByOrNull {
                item -> abs(viewportCenter - (item.offset + item.size / 2))
            }

            closestItemToCenter?.index?.let {
                groups[it].background
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
        items(groups) { group ->
            TopHomeCard(
                cardWidth = cardWidth,
                modifier = Modifier.size(cardWidth, cardHeight),
                onViewProduct = onViewProduct,
                topHomeGroup = group,
            )
        }
    }
}

@Composable
private fun TopHomeCard(
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    onViewProduct: () -> Unit = {},
    topHomeGroup: TopHomeGroup,
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val itemSpacing = dimensionResource(R.dimen.padding_small)
    val cardPadding = paddingMedium

    val cardBackground = topHomeGroup.background
    val titleForeground = remember(cardBackground) {
        getContrastColor(cardBackground)
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .background(cardBackground)
                .padding(cardPadding)
        ) {
            Text(
                color = titleForeground,
                text = topHomeGroup.title,
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(Modifier.height(paddingMedium))

            ItemDisplayWindow(
                cardPadding = cardPadding,
                cardWidth = cardWidth,
                items = topHomeGroup.items,
                itemSpacing = itemSpacing,
                modifier = Modifier.fillMaxSize(),
                onViewProduct = onViewProduct,
            )
        }
    }
}

private fun getContrastColor(color: Color): Color {
    return if (ColorUtils.calculateLuminance(color.toArgb()) > 0.3) {
        Color.Black
    } else {
        Color.White
    }
}
