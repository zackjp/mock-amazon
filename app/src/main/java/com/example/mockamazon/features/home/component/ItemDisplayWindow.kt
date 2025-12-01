package com.example.mockamazon.features.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.mockamazon.R
import com.example.mockamazon.features.home.model.DisplayableItem
import com.example.mockamazon.features.home.model.toDisplayableItem
import com.example.mockamazon.shared.model.Item
import kotlin.math.roundToInt

val DISCOUNT_RED: Color = Color(0xFFC60B37)
val ITEM_BG_COLOR: Color = Color(0xFFF7F7F7)

@Composable
fun ItemDisplayWindow(
    cardPadding: Dp,
    cardWidth: Dp,
    items: List<Item>,
    itemSpacing: Dp,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit = {},
) {
    val itemWidth = (cardWidth - cardPadding * 2 - itemSpacing - 1.dp) / 2
    val maxItemsInEachColumn = when (items.size) {
        2 -> 1
        3, 4 -> 2
        5 -> 3
        else -> 3
    }

    val reversedBottomUpItems = remember(items) {
        items
            .reversed()
            .chunked(maxItemsInEachColumn)
            .map { it.reversed() }
            .flatten()
    }
    val originalLayoutDirection = LocalLayoutDirection.current

    // We want to match the Amazon App experience in which the start column renders
    // the "tall" items (ie, the column with non-max items). We can emulate this
    // by rendering the FlowColumn in reverse order so that the 'start' side is the
    // final column.
    CompositionLocalProvider(LocalLayoutDirection provides originalLayoutDirection.opposite()) {
        FlowColumn(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            maxItemsInEachColumn = maxItemsInEachColumn,
            verticalArrangement = Arrangement.spacedBy(itemSpacing),
        ) {
            // Render each item in the original layout direction
            CompositionLocalProvider(LocalLayoutDirection provides originalLayoutDirection) {
                repeat(reversedBottomUpItems.size) { i ->
                    val itemModifier = Modifier
                        .width(itemWidth)
                        .weight(1f)

                    val item = reversedBottomUpItems[i]
                    ItemDisplay(
                        item = item.toDisplayableItem(),
                        modifier = itemModifier.clickable { onViewProduct(item.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemDisplay(
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

private fun LayoutDirection.opposite(): LayoutDirection {
    return when (this) {
        LayoutDirection.Ltr -> LayoutDirection.Rtl
        LayoutDirection.Rtl -> LayoutDirection.Ltr
    }
}