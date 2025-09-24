package com.example.fakeamazon

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.example.fakeamazon.features.home.HomeScreenRoot
import com.example.fakeamazon.ui.theme.FakeAmazonTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val bottomNavItems = listOf(
        BottomNavItem(Icons.Outlined.Home),
        BottomNavItem(Icons.Outlined.Person),
        BottomNavItem(Icons.Outlined.ShoppingCart),
        BottomNavItem(Icons.Outlined.Menu),
    )

    var navChipsHeightPx by remember { mutableFloatStateOf(0f) }
    val collapsibleState = rememberCollapsibleState(maxCollapseHeightPx = -navChipsHeightPx)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(collapsibleState.scrollObserver),
        topBar = {
            AmazonTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                navChipsOffset = collapsibleState.currentOffsetPx.value,
                offsetFraction = collapsibleState.offsetFraction.value,
                onNavChipsSizeChange = { intSize ->
                    navChipsHeightPx = intSize.height.toFloat()
                },
            )
        },
        bottomBar = {
            AmazonBottomAppBar(
                modifier = Modifier.height(80.dp),
                navItems = bottomNavItems,
            )
        },
    ) { innerPadding ->
        HomeScreenRoot(
            innerPadding = innerPadding,
            modifier = Modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AmazonTopAppBar(
    modifier: Modifier = Modifier,
    navChipsOffset: Float,
    @FloatRange(0.0, 1.0) offsetFraction: Float,
    onNavChipsSizeChange: (IntSize) -> Unit,
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingLarge = dimensionResource(R.dimen.padding_large)

    val navChips = listOf(
        "Early Prime Deals",
        "Groceries",
        "Haul",
        "Medical Care",
        "Same-Day",
        "Pharmacy",
        "In-Store Code",
        "Alexa Lists",
        "Prime",
        "Video",
        "Music",
        "Customer Service"
    )

    val topBarBackgroundColorStart = Color.White.copy(alpha = offsetFraction)
    val topBarBackgroundColorEnd = Color.White.copy(alpha = .75f * offsetFraction)

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    translationY = navChipsOffset
                }
                .background(
                    Brush.verticalGradient(
                        0f to topBarBackgroundColorStart,
                        .45f to topBarBackgroundColorStart,
                        .7f to topBarBackgroundColorEnd
                    )
                )
        )

        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            SimpleSearchBar(
                modifier = Modifier
                    .padding(
                        start = paddingLarge,
                        end = paddingLarge,
                    )
            )

            NavChipsRow(
                modifier = Modifier
                    .onSizeChanged(onNavChipsSizeChange)
                    .padding(top = paddingMedium)
                    .graphicsLayer {
                        alpha = (1 - offsetFraction)
                        translationY = navChipsOffset
                    },
                navigationChips = navChips,
            )
        }
    }
}

@Composable
private fun AmazonBottomAppBar(
    modifier: Modifier = Modifier,
    navItems: List<BottomNavItem>,
) {
    NavigationBar(modifier) {
        navItems.forEach { navItem ->
            NavigationBarItem(
                selected = (navItem.icon === Icons.Outlined.Home),
                onClick = {},
                icon = { Icon(contentDescription = null, imageVector = navItem.icon) },
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SimpleSearchBar(modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }

    SearchBar(
        colors = SearchBarDefaults.colors(),
        expanded = false,
        modifier = modifier,
        onExpandedChange = {},
        inputField = {
            BasicTextField(
                value = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                onValueChange = {},
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {}),
                decorationBox = @Composable { innerTextField ->
                    val colors = TextFieldDefaults.colors().copy(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedLeadingIconColor = Color.Black,
                        unfocusedLeadingIconColor = Color.Black,
                    )

                    TextFieldDefaults.DecorationBox(
                        colors = colors,
                        contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                            start = 0.dp,
                            end = 0.dp,
                            top = 0.dp,
                            bottom = 0.dp
                        ),
                        value = "",
                        innerTextField = innerTextField,
                        enabled = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        placeholder = { Text(stringResource(R.string.search_bar_placeholder)) },
                        singleLine = true,
                        shape = MaterialTheme.shapes.extraLarge,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        container = {
                            TextFieldDefaults.Container(
                                colors = colors,
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                shape = MaterialTheme.shapes.extraLarge,
                            )
                        }
                    )
                }
            )
        },
    ) { }
}

@Composable
private fun NavChipsRow(modifier: Modifier, navigationChips: List<String>) {
    val paddingXSmall = dimensionResource(R.dimen.padding_xsmall)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val translucentWhite = Color(0x99FFFFFF)

    LazyRow(
        contentPadding = PaddingValues(horizontal = paddingLarge),
        horizontalArrangement = Arrangement.spacedBy(paddingSmall),
        modifier = modifier,
    ) {
        items(navigationChips) { item ->
            Text(
                text = item,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(translucentWhite)
                    .padding(
                        vertical = paddingXSmall,
                        horizontal = paddingMedium
                    ),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun rememberCollapsibleState(maxCollapseHeightPx: Float): CollapsibleState {
    val coroutineScope = rememberCoroutineScope()
    return remember(maxCollapseHeightPx, coroutineScope) {
        CollapsibleState(
            coroutineScope = coroutineScope,
            maxCollapseHeight = maxCollapseHeightPx,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    FakeAmazonTheme {
        HomeScreenRoot(modifier = Modifier.fillMaxSize())
    }
}

private data class BottomNavItem(
    val icon: ImageVector
)

private class CollapsibleState(
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
