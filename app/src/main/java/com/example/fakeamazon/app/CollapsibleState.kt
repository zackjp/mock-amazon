package com.example.fakeamazon.app

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun rememberCollapsibleState(maxCollapseHeightPx: Float): CollapsibleState {
    val coroutineScope = rememberCoroutineScope()
    return remember(maxCollapseHeightPx, coroutineScope) {
        CollapsibleState(
            coroutineScope = coroutineScope,
            maxCollapseHeight = maxCollapseHeightPx,
        )
    }
}

class CollapsibleState(
    val coroutineScope: CoroutineScope,
    val maxCollapseHeight: Float,
) {

    val currentOffsetPx = Animatable(0f)
    val offsetFraction = Animatable(0f)

    val scrollObserver =
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val isScrollingAwayFromTop = consumed.y < 0.0f
                val isOverscrollingAtTop = consumed.y == 0.0f && available.y > 0.0f
                val deltaToApply: Float? = when {
                    // offset the collapsible view by the same amount that main content
                    // is scrolling away from the top
                    isScrollingAwayFromTop -> consumed.y

                    // re-enter the collapsible back into view only when overscrolling
                    // from the top, by the amount overscrolled
                    isOverscrollingAtTop -> available.y

                    else -> null
                }

                deltaToApply?.let { delta ->
                    val newTarget = (currentOffsetPx.value + delta).coerceIn(maxCollapseHeight, 0.0f)
                    val newFraction = newTarget / maxCollapseHeight
                    coroutineScope.launch {
                        currentOffsetPx.snapTo(newTarget)
                        offsetFraction.snapTo(newFraction)
                    }
                }

                return super.onPostScroll(consumed, available, source)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val isOverfling = available.y > 0 && available.y > consumed.y

                // when the user attempts to fling beyond the top of main content,
                // we animate the collapsible back in smoothly
                if (isOverfling) {
                    val delta = available.y
                    val newTarget = (currentOffsetPx.value + delta).coerceIn(maxCollapseHeight, 0.0f)
                    val newFraction = newTarget / maxCollapseHeight
                    coroutineScope.launch {
                        // launching two coroutines allows the animations to occur simultaneously
                        // rather than sequentially. it works fine but doesn't guarantee 100%
                        // they're sync'ed like Transition<?> does, so would be nice to clean this up
                        coroutineScope {
                            launch {
                                currentOffsetPx.animateTo(animationSpec = tween(250), targetValue = newTarget)
                            }
                            launch {
                                offsetFraction.animateTo(animationSpec = tween(250), targetValue = newFraction)
                            }
                        }
                    }
                }
                return super.onPostFling(consumed, available)
            }
        }
}
