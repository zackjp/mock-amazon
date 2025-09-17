package com.example.fakeamazon.features.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.fakeamazon.R
import com.example.fakeamazon.features.home.component.ItemDisplay
import com.example.fakeamazon.features.home.model.DisplayableItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopHomeSection(modifier: Modifier) {
    Row(modifier = modifier) {
        val cardWidth = dimensionResource(R.dimen.top_home_card_width)
        val cardHeight = dimensionResource(R.dimen.top_home_card_height)
        val paddingSmall = dimensionResource(R.dimen.padding_small)
        val paddingMedium = dimensionResource(R.dimen.padding_medium)
        val cardPadding = paddingMedium
        val itemSpacing = paddingSmall

        Card(modifier = Modifier.size(cardWidth, cardHeight)) {
            Column(modifier = Modifier.padding(cardPadding)) {
                Text(text = "More top\npicks for you", style = MaterialTheme.typography.titleLarge)

                Spacer(Modifier.height(paddingMedium))

                val items = listOf(
                    DisplayableItem(imageId = R.drawable.item_headphones),
                    DisplayableItem(imageId = R.drawable.item_backpack),
                    DisplayableItem(imageId = R.drawable.item_matcha),
                    DisplayableItem(imageId = R.drawable.item_handsoap),
                    DisplayableItem(imageId = R.drawable.item_detergent),
                )

                FlowColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                    maxItemsInEachColumn = 3,
                    verticalArrangement = Arrangement.spacedBy(itemSpacing),
                ) {
                    val itemWidth = (cardWidth - cardPadding * 2 - itemSpacing) / 2 - 1.dp

                    repeat(items.size) { i ->
                        val itemModifier = Modifier
                            .width(itemWidth)
                            .weight(1f)

                        val item = items[i]
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
