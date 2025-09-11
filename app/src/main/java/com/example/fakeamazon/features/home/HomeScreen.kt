package com.example.fakeamazon.features.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R
import kotlin.math.roundToInt

val DISCOUNT_RED: Color = Color(0xFFCC0020)

@Composable
fun HomeScreenRoot(modifier: Modifier) {
    RecommendedDealsSection(modifier.padding(horizontal = 8.dp))
}

@Composable
fun RecommendedDealsSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        val cardWidth = dimensionResource(R.dimen.recommended_deals_card_width)
        val paddingSmall = dimensionResource(R.dimen.padding_small)

        Text(text = stringResource(R.string.recommended_deals_for_you_section_title), style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(paddingSmall))

        RecommendedDealsCard(Modifier.width(cardWidth))
    }
}

@Composable
private fun RecommendedDealsCard(
    modifier: Modifier = Modifier
) {
    val itemHeight = dimensionResource(R.dimen.recommended_deals_item_height)
    val paddingSmall = dimensionResource(R.dimen.padding_small)

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(paddingSmall)
        ) {
            Text(stringResource(R.string.recommended_deals_for_you))

            val itemModifier = Modifier
                .background(Color.White)
                .height(itemHeight)
                .weight(1f)
                .padding(paddingSmall)

            Row(modifier = Modifier.wrapContentSize()) {
                RecommendedItem(
                    discount = 0.17f,
                    imageRes = R.drawable.item_backpack,
                    modifier = itemModifier
                )

                Spacer(modifier = Modifier.width(paddingSmall))

                RecommendedItem(
                    discount = 0.20f,
                    imageRes = R.drawable.item_headphones,
                    modifier = itemModifier
                )
            }

            Spacer(modifier = Modifier.height(paddingSmall))

            Row(modifier = Modifier.wrapContentSize()) {
                RecommendedItem(
                    discount = 0.12f,
                    imageRes = R.drawable.item_detergent,
                    modifier = itemModifier
                )

                Spacer(modifier = Modifier.width(paddingSmall))

                RecommendedItem(
                    discount = 0.13f,
                    imageRes = R.drawable.item_dishwash_detergent,
                    modifier = itemModifier
                )
            }
        }
    }
}

@Composable
private fun RecommendedItem(
    modifier: Modifier = Modifier,
    discount: Float,
    @DrawableRes imageRes: Int
) {
    val discountPercent = (discount * 100).roundToInt()

    Column(modifier = modifier) {
        Image(
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .padding(4.dp),
            painter = painterResource(imageRes),
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