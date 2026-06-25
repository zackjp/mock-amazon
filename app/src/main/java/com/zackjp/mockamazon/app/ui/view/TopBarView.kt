package com.zackjp.mockamazon.app.ui.view

import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zackjp.mockamazon.R
import com.zackjp.mockamazon.shared.theme.AmazonOutlineMedium
import com.zackjp.mockamazon.shared.theme.AmazonPrimeBlue
import com.zackjp.mockamazon.shared.R as SharedR

@Immutable
private data class NavChipData(
    @param:StringRes val labelId: Int,
    val color: Color,
)

private val NAV_CHIPS = listOf(
    NavChipData(
        labelId = R.string.top_app_bar_nav_chip_grocery,
        color = Color(0xFF09A300),
    ),
    NavChipData(
        labelId = R.string.top_app_bar_nav_chip_haul,
        color = Color(0xFF5A20C9),
    ),
    NavChipData(
        labelId = R.string.top_app_bar_nav_chip_luxury,
        color = Color.Black,
    ),
    NavChipData(
        labelId = R.string.top_app_bar_nav_chip_prime_video,
        color = AmazonPrimeBlue,
    ),
    NavChipData(
        labelId = R.string.top_app_bar_nav_chip_pharmacy,
        color = Color(0xFF029286),
    ),
    NavChipData(
        labelId = R.string.top_app_bar_nav_chip_prime_day,
        color = AmazonPrimeBlue,
    ),
    NavChipData(
        labelId = R.string.top_app_bar_nav_chip_see_all,
        color = Color.Black,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmazonTopAppBarWithNavChips(
    modifier: Modifier = Modifier,
    navChipsOffsetProvider: () -> Float,
    @FloatRange(0.0, 1.0) offsetFractionProvider: () -> Float,
    onNavChipsSizeChange: (IntSize) -> Unit,
    onOpenSearch: () -> Unit = {},
    windowPadding: PaddingValues,
) {
    val paddingXXSmall = dimensionResource(R.dimen.padding_xxsmall)
    val paddingLarge = dimensionResource(R.dimen.padding_large)

    val layoutDirection = LocalLayoutDirection.current

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    translationY = navChipsOffsetProvider()
                }
                .drawWithCache {
                    val topBarBackgroundColorStart = Color.White.copy(alpha = offsetFractionProvider())
                    val topBarBackgroundColorEnd = Color.White.copy(alpha = .75f * offsetFractionProvider())

                    val verticalGradient = Brush.verticalGradient(
                        0f to topBarBackgroundColorStart,
                        .45f to topBarBackgroundColorStart,
                        .7f to topBarBackgroundColorEnd
                    )
                    onDrawBehind {
                        drawRect(
                            brush = verticalGradient,
                        )
                    }
                }
        )

        Column(
            modifier = Modifier
                .padding(
                    end = windowPadding.calculateEndPadding(layoutDirection),
                    start = windowPadding.calculateStartPadding(layoutDirection),
                    top = windowPadding.calculateTopPadding(),
                )
        ) {
            SimpleSearchBar(
                initialSearchText = "",
                backStackSize = 0,
                isSearchEditable = false,
                modifier = Modifier
                    .padding(
                        start = paddingLarge,
                        end = paddingLarge,
                        bottom = 4.dp,
                    ),
                onOpenSearch = onOpenSearch,
            )

            NavChipsRow(
                modifier = Modifier
                    .onSizeChanged(onNavChipsSizeChange)
                    .padding(top = paddingXXSmall)
                    .graphicsLayer {
                        alpha = (1 - offsetFractionProvider())
                        translationY = navChipsOffsetProvider()
                    },
                navigationChips = NAV_CHIPS,
            )
        }
    }

}

@Composable
fun AmazonTopAppBar(
    initialSearchText: String,
    backStackSize: Int,
    isSearchEditable: Boolean,
    modifier: Modifier = Modifier,
    onOpenSearch: () -> Unit = {},
    onPerformSearch: (String) -> Unit,
    onNavigateUp: (() -> Unit)?,
    globalSearchViewModel: GlobalSearchBarViewModel,
    windowPadding: PaddingValues,
) {
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val layoutDirection = LocalLayoutDirection.current

    Row(
        modifier = modifier
            .padding(
                end = windowPadding.calculateEndPadding(layoutDirection),
                start = windowPadding.calculateStartPadding(layoutDirection),
                top = windowPadding.calculateTopPadding()
            )
            .height(IntrinsicSize.Min)
    ) {
        val showUpNavigation = onNavigateUp != null
        if (showUpNavigation) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
                IconButton(
                    onClick = onNavigateUp,
                    modifier = Modifier.fillMaxHeight(),
                ) {
                    Icon(
                        contentDescription = null,
                        painter = painterResource(SharedR.drawable.ic_sharp_arrow_left_alt_24),
                    )
                }
            }
        }

        SimpleSearchBar(
            initialSearchText = initialSearchText,
            backStackSize = backStackSize,
            isSearchEditable = isSearchEditable,
            modifier = Modifier
                .padding(
                    start = if (showUpNavigation) 0.dp else paddingLarge,
                    end = paddingLarge,
                    bottom = 6.dp,
                ),
            onOpenSearch = onOpenSearch,
            onPerformSearch = onPerformSearch,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SimpleSearchBar(
    initialSearchText: String,
    backStackSize: Int,
    isSearchEditable: Boolean,
    modifier: Modifier = Modifier,
    onOpenSearch: () -> Unit = {},
    onPerformSearch: (String) -> Unit = {},
) {
    val textFieldShape = MaterialTheme.shapes.extraLarge
    val currentOnOpenSearch by rememberUpdatedState(onOpenSearch)
    val searchText = rememberSaveable(
        initialSearchText,
        backStackSize, // forces search text to reinitialize when navigating between search result screens
        saver = TextFieldState.Saver,
    ) {
        TextFieldState(
            initialText = initialSearchText,
        )
    }

    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                currentOnOpenSearch()
            }
        }
    }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    SearchBar(
        colors = SearchBarDefaults.colors(),
        expanded = false,
        modifier = modifier,
        onExpandedChange = {},
        windowInsets = WindowInsets(0), // control the window padding in top app bar ourselves
        inputField = {
            BasicTextField(
                interactionSource = interactionSource,
                readOnly = !isSearchEditable,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(textFieldShape)
                    .border(0.5.dp, AmazonOutlineMedium, textFieldShape)
                    .focusable()
                    .focusRequester(focusRequester),
                state = searchText,
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                onKeyboardAction = { onPerformSearch(searchText.text.toString()) },
                textStyle = MaterialTheme.typography.bodyLarge,
                decorator = @Composable { innerTextField ->
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
                                painter = painterResource(SharedR.drawable.ic_outline_search_24),
                                contentDescription = null
                            )
                        },
                        placeholder = {
                            if (searchText.text.isEmpty()) {
                                Text(stringResource(R.string.search_bar_placeholder))
                            }
                        },
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                    )
                }
            )
        },
    ) { }
}

@Composable
private fun NavChipsRow(
    modifier: Modifier = Modifier,
    navigationChips: List<NavChipData>,
) {
    val navChipFont = MaterialTheme.typography.displayMedium.copy(fontSize = 16.sp)

    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val locationIconData = remember { createLocationIconData(navChipFont) }

    Box(
        modifier = modifier
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = paddingLarge),
            horizontalArrangement = Arrangement.spacedBy(paddingSmall),
        ) {
            item {
                NavChip {
                    Text(
                        inlineContent = locationIconData.inlineContent,
                        style = navChipFont,
                        text = locationIconData.text,
                    )
                }
            }

            items(navigationChips) { navChip ->
                NavChip {
                    Text(
                        style = navChipFont,
                        text = stringResource(navChip.labelId),
                        color = navChip.color,
                    )
                }
            }
        }
    }
}

@Composable
private fun NavChip(
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    AssistChip(
        border = null,
        shape = MaterialTheme.shapes.medium,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color.White,
        ),
        onClick = onClick,
        label = {
            Box(
                modifier = Modifier.padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        },
    )
}

/**
 * Use text-embedded icons to let the icons and nav chips text share the same size,
 * including if system font is increased for accessibility reasons. This creates a
 * consistent visual appearance between the [location icon] and the other nav chips,
 * which are all text-only.
 */
private fun createLocationIconData(font: TextStyle): LocationIconData {
    val locationIconId = "location_icon"
    val downIconId = "down_icon"

    val locationIconString = buildAnnotatedString {
        appendInlineContent(locationIconId, "[loc]")
        appendInlineContent(downIconId, "[dwn]")
    }

    val placeholder = Placeholder(
        width = font.fontSize,
        height = font.fontSize,
        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
    )

    val inlineContent = mapOf(
        locationIconId to InlineTextContent(placeholder = placeholder) {
            Icon(painterResource(SharedR.drawable.ic_outline_location_on_24), null)
        },
        downIconId to InlineTextContent(placeholder = placeholder) {
            Icon(painterResource(SharedR.drawable.ic_outline_keyboard_arrow_down_24), null)
        }
    )

    return LocationIconData(locationIconString, inlineContent)
}

private data class LocationIconData(
    val text: AnnotatedString,
    val inlineContent: Map<String, InlineTextContent>,
)
