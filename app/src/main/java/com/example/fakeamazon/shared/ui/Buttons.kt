package com.example.fakeamazon.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.ui.theme.Yellow80


@Composable
fun PrimaryCta(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    text: String,
) {
    Button(
        enabled = enabled,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = Yellow80,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        onClick = onClick,
        modifier = modifier,
    ) {
        val bodySmall = MaterialTheme.typography.bodySmall
        val bodyLarge = MaterialTheme.typography.bodyLarge
        Text(
            autoSize = TextAutoSize.StepBased(
                minFontSize = bodySmall.fontSize,
                maxFontSize = bodyLarge.fontSize,
            ),
            style = bodyLarge,
            text = text,
        )
    }
}

@Composable
fun CartItemQuantityChip(quantity: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.White, MaterialTheme.shapes.large)
            .border(
                width = 2.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
            text = "$quantity",
            textAlign = TextAlign.Center,
        )
    }
}
