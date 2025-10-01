package com.example.fakeamazon.features.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.fakeamazon.R
import com.example.fakeamazon.features.home.model.DisplayableItem
import kotlin.math.roundToInt

val DISCOUNT_RED: Color = Color(0xFFC60B37)
val ITEM_BG_COLOR: Color = Color(0xFFF7F7F7)

@Composable
fun ItemDisplay(
    item: DisplayableItem,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(ITEM_BG_COLOR)
            .padding(dimensionResource(R.dimen.padding_xxsmall))
    ) {
        val paddingSmall = dimensionResource(R.dimen.padding_small)
        val paddingXSmall = dimensionResource(R.dimen.padding_xsmall)

        val (imageRef, discountTextRef, discountRef) = createRefs()
        var showDiscount = item.discount != null

        item.discount?.let {
            showDiscount = true
            val discountPercent = (it * 100).roundToInt()

            Text(
                color = DISCOUNT_RED,
                text = stringResource(R.string.item_display_discount_limited_time),
                modifier = Modifier.constrainAs(discountTextRef) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
                style = MaterialTheme.typography.labelSmall,
            )

            Text(
                color = ITEM_BG_COLOR,
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(color = DISCOUNT_RED)
                    .padding(horizontal = paddingSmall, vertical = paddingXSmall)
                    .constrainAs(discountRef) {
                        start.linkTo(parent.start)
                        bottom.linkTo(discountTextRef.top, margin = paddingXSmall)
                    },
                text = stringResource(
                    R.string.item_display_discount_off_label, discountPercent
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Image(
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(ITEM_BG_COLOR, BlendMode.Multiply),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(imageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    if (showDiscount) {
                        bottom.linkTo(discountRef.top, 4.dp)
                    } else {
                        bottom.linkTo(parent.bottom)
                    }

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(4.dp),
            painter = painterResource(item.imageId),
        )
    }
}
