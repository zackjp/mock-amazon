package com.zackjp.mockamazon.feature.product.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zackjp.mockamazon.shared.ignoreParentPadding
import com.zackjp.mockamazon.shared.theme.AmazonOrange
import com.zackjp.mockamazon.shared.theme.LinkBlue
import com.zackjp.mockamazon.shared.theme.Teal60
import com.zackjp.mockamazon.shared.ui.DotIndicators
import kotlinx.coroutines.launch
import com.zackjp.mockamazon.shared.R as SharedR


@Composable
fun ProductImagesView(
    mainContentPadding: Dp,
    modifier: Modifier = Modifier,
    imageId: Int,
) {
    Column(modifier = modifier) {
        val imageCount = 8
        val wrapAroundScrollPageCount = imageCount + 2 // 1 for each empty page at either end
        val pageState = rememberPagerState(initialPage = 1) { wrapAroundScrollPageCount }
        val coroutineScope = rememberCoroutineScope()
        var isWrapping by remember { mutableStateOf(false) }
        val lastPageIndex = wrapAroundScrollPageCount - 1

        LaunchedEffect(pageState) {
            snapshotFlow {
                pageState.settledPage
            }.collect { settledPage ->
                // early return prevents infinite scroll loop. to properly animate the opposite
                // end's image in from the left/right, we first need to settle on the opposite end's
                // empty page. without this check, both empty pages would just scroll to each other
                // indefinitely
                if (isWrapping) return@collect

                if (settledPage == 0) {
                    isWrapping = true
                    pageState.scrollToPage(lastPageIndex)
                    coroutineScope.launch {
                        pageState.animateScrollToPage(lastPageIndex - 1)
                        isWrapping = false
                    }
                } else if (settledPage == lastPageIndex) {
                    isWrapping = true
                    pageState.scrollToPage(0)
                    coroutineScope.launch {
                        pageState.animateScrollToPage(1)
                        isWrapping = false
                    }
                }
            }
        }

        HorizontalPager(
            contentPadding = PaddingValues(horizontal = mainContentPadding),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .ignoreParentPadding(mainContentPadding),
            pageSpacing = mainContentPadding,
            state = pageState,
            userScrollEnabled = !isWrapping
        ) { page ->
            val filterWithWrapAroundScroll = (page - 1) % 4 // -1, since first image starts at index 1
            val colorFilter: ColorFilter? = when (filterWithWrapAroundScroll) {
                0 -> null
                1 -> ColorFilter.tint(LinkBlue, BlendMode.Color)
                2 -> ColorFilter.tint(AmazonOrange, BlendMode.Color)
                else -> ColorFilter.tint(Teal60, BlendMode.Color)
            }

            // Leave the first and last page empty, which will automatically be scrolled away from
            if (page in 1..lastPageIndex - 1) {
                Image(
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(imageId),
                    colorFilter = colorFilter
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val currentPageExcludingEmptyPages = (pageState.currentPage - 1).coerceIn(0, imageCount - 1)
            DotIndicators(
                currentPage = currentPageExcludingEmptyPages,
                modifier = Modifier.align(Alignment.Center),
                totalPageCount = imageCount,
            )

            FavoriteAndShareActions(
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }
}

@Composable
private fun FavoriteAndShareActions(
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Icon(
            contentDescription = null,
            painter = painterResource(SharedR.drawable.ic_outline_favorite_24),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            contentDescription = null,
            painter = painterResource(SharedR.drawable.ic_outline_share_24),
        )
    }
}
