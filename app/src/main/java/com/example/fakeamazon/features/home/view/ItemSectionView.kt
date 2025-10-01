package com.example.fakeamazon.features.home.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fakeamazon.R
import com.example.fakeamazon.base.ignoreParentPadding
import com.example.fakeamazon.features.home.component.ItemDisplay
import com.example.fakeamazon.features.home.model.toDisplayableItem
import com.example.fakeamazon.model.ItemGroup
import com.example.fakeamazon.model.ItemSection

val ITEM_GROUP_CARD_BORDER_COLOR: Color = Color(0xFFD0D4D4)

@Composable
fun ItemSectionView(
    mainContentHorizontalPadding: Dp,
    modifier: Modifier = Modifier,
    itemSection: ItemSection,
) {
    Column(
        modifier = modifier
    ) {
        val paddingSmall = dimensionResource(R.dimen.padding_small)

        Text(
            text = itemSection.title,
            style = MaterialTheme.typography.displayMedium,
        )

        Spacer(modifier = Modifier.height(paddingSmall))

        LazyRow(
            contentPadding = PaddingValues(horizontal = mainContentHorizontalPadding),
            modifier = Modifier
                .fillMaxWidth()
                .ignoreParentPadding(mainContentHorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(paddingSmall),
        ) {
            items(itemSection.itemGroups) { itemGroup ->
                ItemSectionCard(
                    itemGroup = itemGroup,
                    modifier = Modifier
                        .wrapContentSize()
                        .width(dimensionResource(R.dimen.item_section_group_card_width)),
                    cardWidth = dimensionResource(R.dimen.item_section_group_card_width)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class) // FlowRow
@Composable
private fun ItemSectionCard(
    itemGroup: ItemGroup,
    modifier: Modifier = Modifier,
    cardWidth: Dp
) {
    val paddingXXSmall = dimensionResource(R.dimen.padding_xxsmall)
    val paddingXSmall = dimensionResource(R.dimen.padding_xsmall)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val cardPadding = paddingMedium
    val itemSpacing = paddingXSmall
    val itemHeight = dimensionResource(R.dimen.item_section_item_height)
    val itemWidth = (cardWidth - cardPadding * 2 - itemSpacing - 1.dp) / 2

    Card(
        border = BorderStroke(1.dp, ITEM_GROUP_CARD_BORDER_COLOR),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(cardPadding)
        ) {
            CardHeader(
                modifier = Modifier.fillMaxWidth(),
                title = itemGroup.title,
            )

            Spacer(modifier = Modifier.height(paddingXXSmall))

            FlowRow(
                maxItemsInEachRow = 2,
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(paddingXSmall),
                horizontalArrangement = Arrangement.spacedBy(paddingXSmall),
            ) {
                repeat(4) { i ->
                    val item = when (i) {
                        0 -> itemGroup.rec1
                        1 -> itemGroup.rec2
                        2 -> itemGroup.rec3
                        else -> itemGroup.rec4
                    }

                    val itemModifier = Modifier
                        .height(itemHeight)
                        .width(itemWidth)

                    ItemDisplay(
                        item = item.toDisplayableItem(),
                        modifier = itemModifier,
                    )
                }
            }
        }
    }
}

@Composable
private fun CardHeader(
    modifier: Modifier = Modifier,
    title: String,
) {
    Row(
        horizontalArrangement =
            Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleSmall,
            text = title,
        )
        Icon(
            contentDescription = null,
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            modifier = Modifier.size(28.dp)
        )
    }
}
