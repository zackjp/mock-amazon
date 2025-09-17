package com.example.fakeamazon.base

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.offset
import kotlin.math.roundToInt


fun Modifier.ignoreParentPadding(padding: Dp): Modifier {
    return this.layout { measurable, constraints ->
        val paddingPx = padding.toPx().roundToInt()
        val placeable = measurable.measure(constraints.offset(horizontal = paddingPx))

        layout(placeable.width + paddingPx, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}
