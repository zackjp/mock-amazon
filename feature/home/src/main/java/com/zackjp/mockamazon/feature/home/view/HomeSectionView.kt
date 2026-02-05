package com.zackjp.mockamazon.feature.home.view

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
import com.zackjp.mockamazon.feature.home.R
import com.zackjp.mockamazon.feature.home.component.ItemDisplayWindow
import com.zackjp.mockamazon.shared.ignoreParentPadding
import com.zackjp.mockamazon.shared.model.ItemGroup
import com.zackjp.mockamazon.shared.model.ItemSection
import com.zackjp.mockamazon.shared.theme.AmazonOutlineLight
import com.zackjp.mockamazon.shared.R as SharedR

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
        val paddingSmall = dimensionResource(SharedR.dimen.padding_small)

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
    val paddingXXSmall = dimensionResource(SharedR.dimen.padding_xxsmall)
    val paddingXSmall = dimensionResource(SharedR.dimen.padding_xsmall)
    val paddingMedium = dimensionResource(SharedR.dimen.padding_medium)
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
            Arrangement.spacedBy(dimensionResource(SharedR.dimen.padding_small)),
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
            painter = painterResource(SharedR.drawable.ic_sharp_chevron_forward_24),
            modifier = Modifier.size(28.dp)
        )
    }
}
