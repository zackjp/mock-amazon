package com.example.fakeamazon.features.product.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R
import com.example.fakeamazon.app.ui.AMAZON_BEIGE
import com.example.fakeamazon.ui.theme.AmazonOrange
import kotlin.math.floor


@Composable
fun StoreNameAndProductRatingView(
    modifier: Modifier = Modifier,
    storeName: String,
    storeInitials: String,
    productRating: Float,
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AMAZON_BEIGE)
                .padding(6.dp)
        ) {
            BasicText(
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 14.sp,
                    maxFontSize = 22.sp,
                ),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.displayMedium,
                text = storeInitials,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = storeName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )

            Row {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Visit the Store",
                )

                val ratingString = buildAnnotatedString {
                    append("$productRating ")
                    append(buildProductStarsString(productRating))
                }

                Text(
                    inlineContent = starRatingsIconMap,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodySmall,
                    text = ratingString,
                )
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
