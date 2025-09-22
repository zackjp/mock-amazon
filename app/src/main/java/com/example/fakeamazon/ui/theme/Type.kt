package com.example.fakeamazon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R

val AmazonEmber = FontFamily(
    Font(R.font.amazon_ember_lt, FontWeight.Light),
    Font(R.font.amazon_ember_rg, FontWeight.Normal),
    Font(R.font.amazon_ember_md, FontWeight.Medium),
    Font(R.font.amazon_ember_bd, FontWeight.Bold),
    Font(R.font.amazon_ember_he, FontWeight.ExtraBold),
)

val AmazonEmberDisplay = FontFamily(
    Font(R.font.amazon_ember_display_he, FontWeight.ExtraBold),
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 30.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-0.75).sp,
    ),
    headlineMedium = TextStyle(
        fontSize = 22.sp,
        fontFamily = AmazonEmberDisplay,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-0.75).sp,
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = AmazonEmber,
        fontWeight = FontWeight.Bold,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = AmazonEmber,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = AmazonEmber,
        fontWeight = FontWeight.Normal,
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        fontFamily = AmazonEmber,
        fontWeight = FontWeight.Normal,
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = AmazonEmber,
        fontWeight = FontWeight.Bold,
    ),
)