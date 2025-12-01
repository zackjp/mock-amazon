package com.example.mockamazon.features.home.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mockamazon.R
import com.example.mockamazon.features.home.component.ItemDisplayWindow
import com.example.mockamazon.shared.ignoreParentPadding
import com.example.mockamazon.shared.model.ItemGroup
import com.example.mockamazon.shared.model.ItemSection
import com.example.mockamazon.ui.theme.AmazonOutlineLight

@Composable
fun HomeSectionView(
    itemSection: ItemSection,
    mainContentHorizontalPadding: Dp,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        val cardWidth = dimensionResource(R.dimen.item_section_group_card_width)
        val cardHeight = dimensionResource(R.dimen.item_section_group_card_height)
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
                    cardWidth = cardWidth,
                    itemGroup = itemGroup,
                    modifier = Modifier.size(cardWidth, cardHeight),
                    onViewProduct = onViewProduct,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class) // FlowRow
@Composable
private fun ItemSectionCard(
    cardWidth: Dp,
    itemGroup: ItemGroup,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
) {
    val paddingXXSmall = dimensionResource(R.dimen.padding_xxsmall)
    val paddingXSmall = dimensionResource(R.dimen.padding_xsmall)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val cardPadding = paddingMedium
    val itemSpacing = paddingXSmall
    val items = remember(itemGroup) {
        listOf(itemGroup.rec1, itemGroup.rec2, itemGroup.rec3, itemGroup.rec4)
    }

    Card(
        border = BorderStroke(0.5.dp, AmazonOutlineLight),
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
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

            ItemDisplayWindow(
                cardPadding = cardPadding,
                cardWidth = cardWidth,
                items = items,
                itemSpacing = itemSpacing,
                modifier = Modifier.fillMaxSize(),
                onViewProduct = onViewProduct,
            )
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
            painter = painterResource(R.drawable.ic_sharp_chevron_forward_24),
            modifier = Modifier.size(28.dp)
        )
    }
}
