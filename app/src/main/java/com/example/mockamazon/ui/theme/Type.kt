package com.example.mockamazon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mockamazon.R

val AmazonEmber = FontFamily(
    Font(R.font.amazon_ember_lt, FontWeight.Light),
    Font(R.font.amazon_ember_rg, FontWeight.Normal),
    Font(R.font.amazon_ember_md, FontWeight.Medium),
    Font(R.font.amazon_ember_bd, FontWeight.Bold),
    Font(R.font.amazon_ember_he, FontWeight.ExtraBold),
)

val AmazonEmberDisplay = FontFamily(
    Font(R.font.amazon_ember_display_lt, FontWeight.Light),
    Font(R.font.amazon_ember_display_rg, FontWeight.Normal),
    Font(R.font.amazon_ember_display_md, FontWeight.Medium),
    Font(R.font.amazon_ember_display_bd, FontWeight.Bold),
    Font(R.font.amazon_ember_display_he, FontWeight.ExtraBold),
)

val Typography = Typography(
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.Bold,
    ),
    labelMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.Bold,
    ),
    labelLarge = TextStyle(
        fontSize = 18.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.Bold,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.Normal,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.Normal,
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = AmazonEmber,
        fontWeight = FontWeight.Bold,
    ),
    displayMedium = TextStyle(
        fontSize = 22.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-0.75).sp,
    ),
    displayLarge = TextStyle(
        fontSize = 30.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-0.75).sp,
    ),
)