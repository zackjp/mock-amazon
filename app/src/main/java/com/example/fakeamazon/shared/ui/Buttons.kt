package com.example.fakeamazon.shared.ui

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
