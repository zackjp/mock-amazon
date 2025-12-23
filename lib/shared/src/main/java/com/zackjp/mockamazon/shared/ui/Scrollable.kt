package com.zackjp.mockamazon.shared.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun DotIndicators(
    currentPage: Int,
    modifier: Modifier = Modifier,
    totalPageCount: Int,
) {
    val maxDotCount = 5
    val dotCount = minOf(maxDotCount, totalPageCount)
    var priorPage by remember { mutableIntStateOf(currentPage) }
    val dotsAnimator = remember { Animatable(0f) }
    val density = LocalDensity.current
    val offsetPx = with(density) { 16.dp.toPx() }

    val bigDot = @Composable { color: Color ->
        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
    }

    val smallDot = @Composable { color: Color ->
        Box(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 1.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
    }

    val isDotsScrollable = totalPageCount > maxDotCount
    if (isDotsScrollable) { // only animate if there are more pages than maxDots
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(currentPage) {
            val scrollDirection = when {
                currentPage > priorPage -> 1
                currentPage < priorPage -> -1
                else -> 0 // no scroll
            }
            priorPage = currentPage

            if (scrollDirection == 0) return@LaunchedEffect

            if ((scrollDirection == 1 && currentPage in 3..(totalPageCount - 3))
                || (scrollDirection == -1 && currentPage in 2..(totalPageCount - 4))) {

                coroutineScope.launch {
                    // Start off to the side then animate back to center
                    dotsAnimator.snapTo(scrollDirection * offsetPx)
                    dotsAnimator.animateTo(0f)
                }

            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.offset(with(density) { dotsAnimator.value.toDp() }),
    ) {
        repeat(dotCount) { dotIndex ->
            // adjustment that keeps the black dot in the middle until the current page is near the
            // start/end. at which point the dot shifts left/right instead of scrolling
            val pageAdjustment = (currentPage - 2).coerceIn(0, totalPageCount - dotCount)
            val dotToPage = pageAdjustment + dotIndex

            val color = if (currentPage == dotToPage) Color.Black else Color.LightGray

            val dotSize = when {
                !isDotsScrollable -> bigDot // if there's no scrolling, always render a big dot
                dotIndex == 0 && currentPage >= 2 -> smallDot
                dotIndex == dotCount - 1 && currentPage < totalPageCount - 2 -> smallDot
                else -> bigDot
            }

            dotSize(color)
        }
    }
}
