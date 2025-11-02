package com.example.fakeamazon.shared.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.roundToInt


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