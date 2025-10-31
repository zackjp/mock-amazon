package com.example.fakeamazon.shared.ui

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun PrimaryCta(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    text: String,
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            style = MaterialTheme.typography.labelLarge,
            text = text,
        )
    }
}
