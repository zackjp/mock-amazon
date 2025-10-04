package com.example.fakeamazon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    onBackground = AmazonBlack,
    onSurface = AmazonBlack,
    onSurfaceVariant = AmazonGray,
    surfaceContainer = Color.White,
    surfaceContainerHighest = Color.White,
)

@Composable
fun FakeAmazonTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}