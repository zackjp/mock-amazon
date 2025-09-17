package com.example.fakeamazon.features.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.fakeamazon.R
import com.example.fakeamazon.features.home.component.ItemDisplay
import com.example.fakeamazon.features.home.model.TopHomeGroup

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopHomeSection(modifier: Modifier, topHomeGroups: List<TopHomeGroup>) {
    val cardWidth = dimensionResource(R.dimen.top_home_card_width)
    val cardHeight = dimensionResource(R.dimen.top_home_card_height)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val cardPadding = paddingMedium
    val itemSpacing = paddingSmall

    LazyRow(
        modifier = modifier,
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(paddingSmall)
    ) {

        items(topHomeGroups) { itemGroup ->
            Card(modifier = Modifier.size(cardWidth, cardHeight)) {
                Column(modifier = Modifier.padding(cardPadding)) {
                    Text(
                        text = itemGroup.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(paddingMedium))

                    val maxItemsInEachColumn = 3

                    val reversedBottomUpItems = itemGroup.items
                        .reversed()
                        .chunked(maxItemsInEachColumn)
                        .map { it.reversed() }
                        .flatten()
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
    }
}

private fun LayoutDirection.opposite(): LayoutDirection {
    return when (this) {
        LayoutDirection.Ltr -> LayoutDirection.Rtl
        LayoutDirection.Rtl -> LayoutDirection.Ltr
    }
}