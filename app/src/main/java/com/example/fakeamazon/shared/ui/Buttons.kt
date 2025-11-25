package com.example.fakeamazon.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R
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
fun CartItemQuantityChip(
    modifier: Modifier = Modifier,
    onIncrement: () -> Unit = {},
    onRemove: () -> Unit = {},
    quantity: Int,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .background(Color.White, MaterialTheme.shapes.large)
            .border(
                width = 2.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconSize = 18.dp
        Icon(
            contentDescription = null,
            modifier = Modifier
                .width(iconSize)
                .aspectRatio(1f)
                .clickable(onClick = onRemove),
            painter = painterResource(R.drawable.ic_outline_delete_24),
        )
        Text(
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
            text = "$quantity",
            textAlign = TextAlign.Center,
        )
        Icon(
            contentDescription = null,
            modifier = Modifier
                .width(iconSize)
                .aspectRatio(1f)
                .clickable(onClick = onIncrement),
            painter = painterResource(R.drawable.ic_sharp_add_24),
        )
    }
}
