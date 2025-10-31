package com.example.fakeamazon.shared.ui

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.fakeamazon.ui.theme.AmazonBlack
import com.example.fakeamazon.ui.theme.AmazonGray
import com.example.fakeamazon.ui.theme.Yellow40
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
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = text,
        )
    }
}
