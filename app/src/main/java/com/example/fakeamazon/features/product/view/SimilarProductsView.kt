package com.example.fakeamazon.features.product.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R
import com.example.fakeamazon.shared.ignoreParentPadding
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.ui.theme.AmazonOrange
import com.example.fakeamazon.ui.theme.LinkBlue
import kotlin.math.floor


@Composable
fun SimilarProductsView(
    horizontalContentPadding: Dp,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
    similarProducts: List<ProductInfo>,
) {
    Column(modifier = modifier) {
        Text(
            style = with(MaterialTheme.typography.bodyLarge) {
                copy(
                    fontSize = fontSize * 1.15,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = stringResource(R.string.you_might_also_like),
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = horizontalContentPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .ignoreParentPadding(horizontalContentPadding),
        ) {
            items(similarProducts) { product ->
                Column(
                    modifier = Modifier
                        .clickable(onClick = { onViewProduct(product.id) })
                        .width(150.dp)
                ) {
                    Image(
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        painter = painterResource(product.imageId),
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        color = LinkBlue,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        text = product.title,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        maxLines = 1,
                        fontSize = 20.sp,
                        inlineContent = starRatingsIconMap,
                        text = buildProductStarsString(product.productRating),
                    )
                }
            }
        }
    }
}

private fun buildProductStarsString(rawProductRating: Float): AnnotatedString {
    val productRating = rawProductRating.coerceAtMost(5f)
    val ratingFraction = productRating - floor(productRating)

    var fullStarCount = productRating.toInt()
    var halfStarCount = 0

    when {
        ratingFraction >= 0.8f -> fullStarCount++ // .8+ = round up to full-star
        ratingFraction >= 0.3f -> halfStarCount = 1 // .3+ = show half-star
        else -> {} // do nothing
    }

    val emptyStarCount = 5 - fullStarCount - halfStarCount // fill remaining with empty-stars

    return buildAnnotatedString {
        repeat(fullStarCount) {
            appendInlineContent("fullStar", "[*]")
        }
        repeat(halfStarCount) {
            appendInlineContent("halfStar", "[/]")
        }
        repeat(emptyStarCount) {
            appendInlineContent("emptyStar", "[-]")
        }
    }
}

private val starRatingsIconMap = mapOf(
    "fullStar" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.Center)) {
        Icon(
            contentDescription = null,
            painter = painterResource(R.drawable.ic_baseline_star_24),
            tint = AmazonOrange,
        )
    },
    "halfStar" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.Center)) {
        Icon(
            contentDescription = null,
            painter = painterResource(R.drawable.ic_outline_star_half_24),
            tint = AmazonOrange,
        )
    },
    "emptyStar" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.Center)) {
        Icon(
            contentDescription = null,
            painter = painterResource(R.drawable.ic_outline_star_24),
            tint = AmazonOrange,
        )
    },
)
