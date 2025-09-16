package com.example.fakeamazon.features.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.example.fakeamazon.features.home.DISCOUNT_RED
import com.example.fakeamazon.features.home.RECOMMENDED_ITEM_BG_COLOR
import com.example.fakeamazon.features.home.model.DisplayableItem
import kotlin.math.roundToInt

@Composable
fun ItemDisplay(
    item: DisplayableItem,
    modifier: Modifier = Modifier,
) {
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
            painter = painterResource(item.imageId),
        )

        item.discount?.let {
            val discountPercent = (it * 100).roundToInt()

            Text(
                color = Color.White,
                modifier = Modifier
                    .background(color = DISCOUNT_RED)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                text = stringResource(
                    R.string.recommended_deals_discount_off_label, discountPercent
                ),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
            )

            Text(
                color = DISCOUNT_RED,
                text = stringResource(R.string.recommended_deals_limited_time),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
            )
        }
    }
}
