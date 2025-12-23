package com.zackjp.mockamazon.shared.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.theme.AmazonOrange
import com.zackjp.mockamazon.shared.theme.AmazonPrimeBlue
import com.zackjp.mockamazon.shared.theme.Orange80
import java.math.RoundingMode
import java.util.Locale
import kotlin.math.floor
import kotlin.math.roundToInt

private val primeLogoInlineContent = buildPrimeTextIconMap()
private val primeLogoText = buildAnnotatedString {
    appendInlineContent("primeCheckmarkIcon", "[primeCheck]")
    withStyle(
        SpanStyle(
            color = AmazonPrimeBlue,
            //fontFamily = AmazonEmber,
            fontWeight = FontWeight.Bold,
        )
    ) {
        append("prime")
    }
}

fun getPrimeLogoTextInfo(): PrimeLogoTextInfo = PrimeLogoTextInfo(
        inlineContent = primeLogoInlineContent,
        primeLogoText = primeLogoText,
    )

data class PrimeLogoTextInfo(
    val inlineContent: Map<String, InlineTextContent>,
    val primeLogoText: AnnotatedString,
)

fun getRatingStarsTextInfo(productRating: Float): RatingStarsTextInfo = RatingStarsTextInfo(
    inlineContent = starRatingsIconMap,
    normalizedRating = productRating.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toFloat(),
    text = buildProductStarsString(productRating),
)

data class RatingStarsTextInfo(
    val inlineContent: Map<String, InlineTextContent>,
    val normalizedRating: Float,
    val text: AnnotatedString,
)

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
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.ic_baseline_star_24),
            tint = AmazonOrange,
        )
    },
    "halfStar" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.Center)) {
        Icon(
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.ic_outline_star_half_24),
            tint = AmazonOrange,
        )
    },
    "emptyStar" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.Center)) {
        Icon(
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.ic_outline_star_24),
            tint = AmazonOrange,
        )
    },
)

@Composable
fun PriceText(
    modifier: Modifier = Modifier,
    priceUSD: Float,
    displaySize: PriceDisplaySize = PriceDisplaySize.SP22,
) {
    val superscriptSize: TextUnit
    val superscriptShift: Float
    val wholeTextSize: TextUnit

    when (displaySize) {
        PriceDisplaySize.SP22 -> {
            superscriptSize = 13.sp
            superscriptShift = 0.3f
            wholeTextSize = 22.sp
        }

        PriceDisplaySize.SP38 -> {
            superscriptSize = 15.sp
            superscriptShift = 0.42f
            wholeTextSize = 38.sp
        }
    }

    val wholePart = priceUSD.toInt()
    val fractionalPart = ((priceUSD - wholePart) * 100).roundToInt()

    val priceText = buildAnnotatedString {
        append(
            AnnotatedString(
                spanStyle = SpanStyle(
                    baselineShift = BaselineShift(superscriptShift),
                    fontSize = superscriptSize,
                    fontWeight = FontWeight.Bold,
                ),
                text = "$",
            )
        )
        append(
            AnnotatedString(
                spanStyle = SpanStyle(fontWeight = FontWeight.Bold),
                text = "$wholePart",
            )
        )
        append(
            AnnotatedString(
                spanStyle = SpanStyle(
                    baselineShift = BaselineShift(superscriptShift),
                    fontSize = superscriptSize,
                    fontWeight = FontWeight.Bold,
                ),
                text = String.format(Locale.getDefault(), "%02d", fractionalPart),
            )
        )
    }

    Text(
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = wholeTextSize),
        text = priceText,
    )
}

enum class PriceDisplaySize {
    SP22,
    SP38,
}

private fun buildPrimeTextIconMap(): Map<String, InlineTextContent> {
    return mapOf(
        "primeCheckmarkIcon" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.TextBottom)) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                contentDescription = null,
                painter = painterResource(R.drawable.baseline_check_24),
                tint = Orange80,
            )
        }
    )
}

@Preview
@Composable
private fun PriceTextPreview() {
    Surface {
        PriceText(
            modifier = Modifier,
            priceUSD = 13.99f,
            displaySize = PriceDisplaySize.SP38
        )
    }
}

@Preview
@Composable
private fun PrimeLogoTextPreview() {
    val primeTextInfo = getPrimeLogoTextInfo()

    Surface {
        Text(
            inlineContent = primeTextInfo.inlineContent,
            text = primeTextInfo.primeLogoText,
            fontSize = 38.sp,
        )
    }
}

@Preview
@Composable
private fun RatingStarsPreview() {
    val ratingTextInfo = getRatingStarsTextInfo(4.35f)

    Surface {
        Text(
            inlineContent = ratingTextInfo.inlineContent,
            text = ratingTextInfo.text,
            fontSize = 38.sp,
        )
    }
}