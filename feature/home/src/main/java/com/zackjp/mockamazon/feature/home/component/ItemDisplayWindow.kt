package com.zackjp.mockamazon.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zackjp.mockamazon.core.model.ProductTile
import com.zackjp.mockamazon.feature.home.R
import kotlin.math.roundToInt
import com.zackjp.mockamazon.shared.R as SharedR

val DISCOUNT_RED: Color = Color(0xFFC60B37)
val ITEM_BG_COLOR: Color = Color(0xFFF7F7F7)

/**
 * TODO: replace List<ProductTile> with ImmutableList
 */
@Composable
fun ItemDisplayWindow(
    modifier: Modifier = Modifier,
    productTiles: List<ProductTile>,
    itemSpacing: Dp,
    displayDiscounts: Boolean = true,
    onViewProduct: (Int) -> Unit = {},
) {
    val cappedProductTiles = productTiles.take(5)
    val splitAt = (cappedProductTiles.size / 2).coerceIn(0, 3)
    val (startColumn, endColumn) = remember(cappedProductTiles) {
        cappedProductTiles.take(splitAt) to cappedProductTiles.drop(splitAt)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        if (startColumn.isNotEmpty()) {
            ProductTileColumn(
                modifier = Modifier.weight(1f),
                productTiles = startColumn,
                displayDiscounts = displayDiscounts,
                onViewProduct = onViewProduct,
                itemSpacing = itemSpacing,
            )
        }

        if (endColumn.isNotEmpty()) {
            ProductTileColumn(
                modifier = Modifier.weight(1f),
                productTiles = endColumn,
                displayDiscounts = displayDiscounts,
                onViewProduct = onViewProduct,
                itemSpacing = itemSpacing,
            )
        }
    }
}

/**
 * TODO: replace List<ProductTile> with ImmutableList
 */
@Composable
private fun ProductTileColumn(
    modifier: Modifier = Modifier,
    productTiles: List<ProductTile>,
    displayDiscounts: Boolean,
    onViewProduct: (Int) -> Unit,
    itemSpacing: Dp
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        productTiles.forEach { productTile ->
            ItemTile(
                item = productTile,
                displayDiscounts = displayDiscounts,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable { onViewProduct(productTile.productId) },
            )
        }
    }
}

@Composable
private fun ItemTile(
    item: ProductTile,
    displayDiscounts: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(ITEM_BG_COLOR)
            .padding(dimensionResource(SharedR.dimen.padding_xxsmall))
    ) {
        val context = LocalContext.current
        val model = remember(item.imageRes) {
            ImageRequest.Builder(context)
                .data(item.imageRes)
                .crossfade(true)
                .build()
        }
        AsyncImage(
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(ITEM_BG_COLOR, BlendMode.Multiply),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(4.dp),
        )

        val discount = item.discount
        if (displayDiscounts && discount != null) {
            val paddingSmall = dimensionResource(SharedR.dimen.padding_small)
            val paddingXSmall = dimensionResource(SharedR.dimen.padding_xsmall)

            val discountPercent = (discount * 100).roundToInt()

            Text(
                color = DISCOUNT_RED,
                text = stringResource(SharedR.string.item_display_discount_limited_time),
                style = MaterialTheme.typography.labelSmall,
            )

            Spacer(modifier = Modifier.height(paddingXSmall))

            Text(
                color = ITEM_BG_COLOR,
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(color = DISCOUNT_RED)
                    .padding(horizontal = paddingSmall, vertical = paddingXSmall),
                text = stringResource(
                    SharedR.string.item_display_discount_off_label, discountPercent
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun ItemDisplayWindowPreview() {
    val cardWidth = dimensionResource(R.dimen.home_hero_card_width)
    val cardHeight = dimensionResource(R.dimen.home_hero_card_aspect_ratio)
    val itemSpacing = dimensionResource(SharedR.dimen.padding_small)

    ItemDisplayWindow(
        modifier = Modifier.size(cardWidth, cardHeight),
        productTiles = listOf(
            ProductTile(
                productId = 1,
                imageRes = SharedR.drawable.item_game_ra,
            ),
            ProductTile(
                productId = 2,
                imageRes = SharedR.drawable.item_game_lost_cities,
            ),
            ProductTile(
                productId = 3,
                imageRes = SharedR.drawable.item_game_forest_shuffle,
            ),
            ProductTile(
                productId = 4,
                imageRes = SharedR.drawable.item_game_monopoly_deal,
            ),
            ProductTile(
                productId = 4,
                imageRes = SharedR.drawable.item_game_catan,
            ),
        ),
        itemSpacing = itemSpacing,
        onViewProduct = {},
    )
}
