package com.example.fakeamazon.shared.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R
import com.example.fakeamazon.ui.theme.AmazonEmber
import com.example.fakeamazon.ui.theme.AmazonPrimeBlue
import com.example.fakeamazon.ui.theme.Orange80
import java.util.Locale
import kotlin.math.roundToInt

private val primeLogoInlineContent = buildPrimeTextIconMap()

@Composable
fun WithPrimeLogoText(block: @Composable (primeLogoInfo: PrimeLogoInfo) -> Unit) {
    val primeLogoAppender: AnnotatedString.Builder.() -> Unit = {
        appendInlineContent("primeCheckmarkIcon", "[primeCheck]")
        withStyle(
            SpanStyle(
                color = AmazonPrimeBlue,
                fontFamily = AmazonEmber,
                fontWeight = FontWeight.Bold,
            )
        ) {
            append("prime")
        }
    }

    val primeLogoInfo = PrimeLogoInfo(
        appendPrimeLogo = primeLogoAppender,
        inlineContent = primeLogoInlineContent,
    )

    block(primeLogoInfo)
}

data class PrimeLogoInfo(
    val appendPrimeLogo: AnnotatedString.Builder.() -> Unit,
    val inlineContent: Map<String, InlineTextContent>,
)

@Composable
fun PriceText(
    modifier: Modifier = Modifier,
    priceUSD: Float,
    displaySize: PriceDisplaySize = PriceDisplaySize.Medium,
) {
    val superscriptSize: TextUnit
    val superscriptShift: Float
    val wholeTextSize: TextUnit

    when (displaySize) {
        PriceDisplaySize.Medium -> {
            superscriptSize = 13.sp
            superscriptShift = 0.3f
            wholeTextSize = 22.sp
        }

        PriceDisplaySize.Large -> {
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
                ),
                text = "$",
            )
        )
        append("$wholePart")
        append(
            AnnotatedString(
                spanStyle = SpanStyle(
                    baselineShift = BaselineShift(superscriptShift),
                    fontWeight = FontWeight.Bold,
                    fontSize = superscriptSize,
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
    Medium,
    Large,
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
