package com.example.fakeamazon.features.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R
import com.example.fakeamazon.data.DealsRepository
import com.example.fakeamazon.model.Recommendation
import com.example.fakeamazon.model.RecommendationGroup
import com.example.fakeamazon.model.toList
import kotlin.math.roundToInt

val DISCOUNT_RED: Color = Color(0xFFC60B37)
val RECOMMENDED_CARD_BORDER_COLOR: Color = Color(0xFFD0D4D4)
val RECOMMENDED_ITEM_BG_COLOR: Color = Color(0xFFF7F7F7)

@Composable
fun HomeScreenRoot(modifier: Modifier) {
    val recommendationGroups = DealsRepository.RECOMMENDED_DEALS

    RecommendedDealsSection(
        modifier = modifier.padding(horizontal = 16.dp),
        recommendationGroups = recommendationGroups
    )
}

@Composable
fun RecommendedDealsSection(
    modifier: Modifier = Modifier,
    recommendationGroups: List<RecommendationGroup>
) {
    Column(
        modifier = modifier
    ) {
        val paddingSmall = dimensionResource(R.dimen.padding_small)

        Text(
            text = stringResource(R.string.recommended_deals_for_you_section_title),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(paddingSmall))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(paddingSmall)
        ) {
            items(recommendationGroups) { recommendationGroup ->
                RecommendedDealsCard(
                    recommendationGroup = recommendationGroup,
                    modifier = Modifier
                        .wrapContentSize()
                        .width(dimensionResource(R.dimen.recommended_deals_card_width))
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class) // FlowRow
@Composable
private fun RecommendedDealsCard(
    recommendationGroup: RecommendationGroup,
    modifier: Modifier = Modifier,
) {
    val itemHeight = dimensionResource(R.dimen.recommended_deals_item_height)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingXSmall = dimensionResource(R.dimen.padding_xsmall)

    Card(
        border = BorderStroke(1.dp, RECOMMENDED_CARD_BORDER_COLOR),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .wrapContentSize()
                .padding(paddingSmall)
        ) {
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(paddingSmall)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.recommended_deals_for_you),
                )
                Icon(
                    contentDescription = null,
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                )
            }

            Spacer(modifier = Modifier.height(paddingSmall))

            val twoChunkedItems = recommendationGroup.toList().chunked(2)
            FlowRow(
                maxItemsInEachRow = 2,
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(paddingXSmall),
            ) {
                repeat(twoChunkedItems.size) { i ->
                    val itemModifier = Modifier
                        .height(itemHeight)
                        .weight(1f)

                    Row(modifier = Modifier.fillMaxWidth()) {
                        RecommendedItem(
                            item = twoChunkedItems[i][0],
                            modifier = itemModifier
                        )

                        if (twoChunkedItems.size > 1) {
                            Spacer(modifier = Modifier.width(paddingXSmall))

                            RecommendedItem(
                                item = twoChunkedItems[i][1],
                                modifier = itemModifier,
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun RecommendedItem(
    modifier: Modifier = Modifier,
    item: Recommendation
) {
    val discountPercent = (item.discount * 100).roundToInt()

    Column(
        modifier = modifier
            .background(RECOMMENDED_ITEM_BG_COLOR)
            .padding(dimensionResource(R.dimen.padding_xxsmall))
    ) {
        Image(
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(RECOMMENDED_ITEM_BG_COLOR, BlendMode.Multiply),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .padding(4.dp),
            painter = painterResource(item.imageRes),
        )

        Text(
            color = Color.White,
            modifier = Modifier
                .background(color = DISCOUNT_RED)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            text = stringResource(
                R.string.recommended_deals_discount_off_label,
                discountPercent
            ),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
        )

        Text(
            color = DISCOUNT_RED,
            text = stringResource(R.string.recommended_deals_limited_time),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
        )
    }
}
