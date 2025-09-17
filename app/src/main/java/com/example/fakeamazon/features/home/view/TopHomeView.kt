package com.example.fakeamazon.features.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.fakeamazon.R
import com.example.fakeamazon.base.ignoreParentPadding
import com.example.fakeamazon.features.home.component.ItemDisplay
import com.example.fakeamazon.features.home.model.TopHomeGroup

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopHomeSection(
    mainContentHorizontalPadding: Dp,
    modifier: Modifier,
    topHomeGroups: List<TopHomeGroup>,
) {
    val cardWidth = dimensionResource(R.dimen.top_home_card_width)
    val cardHeight = dimensionResource(R.dimen.top_home_card_height)
    val paddingSmall = dimensionResource(R.dimen.padding_small)

    LazyRow(
        contentPadding = PaddingValues(horizontal = mainContentHorizontalPadding),
        modifier = modifier.ignoreParentPadding(mainContentHorizontalPadding),
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(paddingSmall)
    ) {
        items(topHomeGroups) { topHomeGroup ->
            TopHomeCard(
                cardWidth = cardWidth,
                modifier = Modifier.size(cardWidth, cardHeight),
                topHomeGroup = topHomeGroup,
            )
        }
    }
}

@Composable
private fun TopHomeCard(
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    topHomeGroup: TopHomeGroup,
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val itemSpacing = dimensionResource(R.dimen.padding_small)
    val cardPadding = paddingMedium
    val maxItemsInEachColumn = 3

    val cardBackground = topHomeGroup.background
    val titleForeground = remember(cardBackground) {
        getContrastColor(cardBackground)
    }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .background(cardBackground)
                .padding(cardPadding)
        ) {
            Text(
                color = titleForeground,
                text = topHomeGroup.title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(paddingMedium))

            val reversedBottomUpItems = remember(topHomeGroup.items, maxItemsInEachColumn) {
                topHomeGroup.items
                    .reversed()
                    .chunked(maxItemsInEachColumn)
                    .map { it.reversed() }
                    .flatten()
            }
            val originalLayoutDirection = LocalLayoutDirection.current

            // We want to match the Amazon App experience in which the start column renders
            // the "tall" items (ie, the column with non-max items). We can emulate this
            // by rendering the FlowColumn in reverse order so that the 'start' side is the
            // final column.
            CompositionLocalProvider(LocalLayoutDirection provides originalLayoutDirection.opposite()) {
                FlowColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                    maxItemsInEachColumn = maxItemsInEachColumn,
                    verticalArrangement = Arrangement.spacedBy(itemSpacing),
                ) {
                    val itemWidth = (cardWidth - cardPadding * 2 - itemSpacing) / 2 - 1.dp

                    // Render each item in the original layout direction
                    CompositionLocalProvider(LocalLayoutDirection provides originalLayoutDirection) {
                        repeat(reversedBottomUpItems.size) { i ->
                            val itemModifier = Modifier
                                .width(itemWidth)
                                .weight(1f)

                            val item = reversedBottomUpItems[i]
                            ItemDisplay(
                                item = item,
                                modifier = itemModifier,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getContrastColor(color: Color): Color {
    return if (ColorUtils.calculateLuminance(color.toArgb()) < 0.5) {
        Color.White
    } else {
        Color.Black
    }
}

private fun LayoutDirection.opposite(): LayoutDirection {
    return when (this) {
        LayoutDirection.Ltr -> LayoutDirection.Rtl
        LayoutDirection.Rtl -> LayoutDirection.Ltr
    }
}