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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R
import kotlin.math.roundToInt

val DISCOUNT_RED: Color = Color(0xFFCC0020)

@Composable
fun HomeScreenRoot(modifier: Modifier) {
    RecommendedDeals(modifier.padding(horizontal = 8.dp))
}

@Composable
fun RecommendedDeals(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Recommended deals for you", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.width(280.dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            ) {
                Text("Deals for you")

                val itemHeight = 200.dp
                val itemModifier = Modifier
                    .background(Color.White)
                    .height(itemHeight)
                    .weight(1f)
                    .padding(8.dp)

                Row(modifier = Modifier.wrapContentSize()) {
                    RecommendedItem(
                        discount = 0.17f,
                        imageRes = R.drawable.item_backpack,
                        modifier = itemModifier
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    RecommendedItem(
                        discount = 0.20f,
                        imageRes = R.drawable.item_headphones,
                        modifier = itemModifier
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.wrapContentSize()) {
                    RecommendedItem(
                        discount = 0.12f,
                        imageRes = R.drawable.item_detergent,
                        modifier = itemModifier
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    RecommendedItem(
                        discount = 0.13f,
                        imageRes = R.drawable.item_dishwash_detergent,
                        modifier = itemModifier
                    )
                }
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

        val discountPercent = (discount * 100).roundToInt()

        Text(
            color = Color.White,
            modifier = Modifier
                .background(color = DISCOUNT_RED)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            text = "$discountPercent% off",
            fontSize = 12.sp,
        )
        Text(
            color = DISCOUNT_RED,
            text = "Limited time deal",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
        )
    }
}