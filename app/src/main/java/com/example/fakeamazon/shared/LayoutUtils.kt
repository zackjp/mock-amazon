package com.example.fakeamazon.shared

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.offset
import kotlin.math.roundToInt


fun Modifier.ignoreParentPadding(horizontalPadding: Dp): Modifier {
    return this.layout { measurable, constraints ->
        val horizontalPaddingPx = horizontalPadding.toPx().roundToInt()
        val placeable = measurable.measure(constraints.offset(horizontal = horizontalPaddingPx * 2))

        layout(placeable.width + horizontalPaddingPx * 2, placeable.height) {
            placeable.placeRelative(horizontalPaddingPx, 0)
        }
    }
}
