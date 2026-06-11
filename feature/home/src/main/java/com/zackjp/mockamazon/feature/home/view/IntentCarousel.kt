package com.zackjp.mockamazon.feature.home.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.zackjp.mockamazon.core.model.ContextCard
import com.zackjp.mockamazon.core.model.IntentCarousel
import com.zackjp.mockamazon.feature.home.R
import com.zackjp.mockamazon.feature.home.component.ItemDisplayWindow
import com.zackjp.mockamazon.shared.ignoreParentPadding
import com.zackjp.mockamazon.shared.theme.AmazonOutlineLight
import com.zackjp.mockamazon.shared.R as SharedR

@Composable
fun IntentCarousel(
    intentCarousel: IntentCarousel,
    mainContentHorizontalPadding: Dp,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        val cardWidth = dimensionResource(R.dimen.intent_carousel_context_card_width)
        val cardHeight = dimensionResource(R.dimen.intent_carousel_context_card_height)
        val paddingSmall = dimensionResource(SharedR.dimen.padding_small)

        Text(
            text = intentCarousel.title,
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
            items(items = intentCarousel.contextCards, key = { it.contextId }) { contextCard ->
                ContextCard(
                    cardWidth = cardWidth,
                    contextCard = contextCard,
                    modifier = Modifier.size(cardWidth, cardHeight),
                    onViewProduct = onViewProduct,
                )
            }
        }
    }
}

@Composable
private fun ContextCard(
    cardWidth: Dp,
    contextCard: ContextCard,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
) {
    val paddingXXSmall = dimensionResource(SharedR.dimen.padding_xxsmall)
    val paddingXSmall = dimensionResource(SharedR.dimen.padding_xsmall)
    val paddingMedium = dimensionResource(SharedR.dimen.padding_medium)
    val cardPadding = paddingMedium
    val itemSpacing = paddingXSmall
    val items = remember(contextCard) {
        listOf(contextCard.rec1, contextCard.rec2, contextCard.rec3, contextCard.rec4)
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
                title = contextCard.title,
            )

            Spacer(modifier = Modifier.height(paddingXXSmall))

            ItemDisplayWindow(
                cardPadding = cardPadding,
                cardWidth = cardWidth,
                productTiles = items,
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
