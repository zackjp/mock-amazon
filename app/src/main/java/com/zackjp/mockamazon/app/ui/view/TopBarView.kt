package com.zackjp.mockamazon.app.ui.view

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zackjp.mockamazon.R
import com.zackjp.mockamazon.shared.theme.AmazonOutlineMedium
import com.zackjp.mockamazon.shared.R as SharedR

val NAV_CHIPS = listOf(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmazonTopAppBarWithNavChips(
    modifier: Modifier = Modifier,
    navChipsOffset: Float,
    @FloatRange(0.0, 1.0) offsetFraction: Float,
    onNavChipsSizeChange: (IntSize) -> Unit,
    onOpenSearch: () -> Unit = {},
    windowPadding: PaddingValues,
) {
    val paddingXXSmall = dimensionResource(R.dimen.padding_xxsmall)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingLarge = dimensionResource(R.dimen.padding_large)

    val topBarBackgroundColorStart = Color.White.copy(alpha = offsetFraction)
    val topBarBackgroundColorEnd = Color.White.copy(alpha = .75f * offsetFraction)

    val layoutDirection = LocalLayoutDirection.current

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
                isSearchEditable = false,
                modifier = Modifier
                    .padding(
                        start = paddingLarge,
                        end = paddingLarge,
                        bottom = paddingSmall
                    ),
                onOpenSearch = onOpenSearch
            )

            NavChipsRow(
                modifier = Modifier
                    .onSizeChanged(onNavChipsSizeChange)
                    .padding(top = paddingXXSmall)
                    .graphicsLayer {
                        alpha = (1 - offsetFraction)
                        translationY = navChipsOffset
                    },
                navigationChips = NAV_CHIPS,
            )
        }
    }

}

@Composable
fun AmazonTopAppBar(
    initialSearchText: String,
    isSearchEditable: Boolean,
    modifier: Modifier = Modifier,
    onOpenSearch: () -> Unit = {},
    onPerformSearch: (String) -> Unit,
    onNavigateUp: (() -> Unit)?,
    windowPadding: PaddingValues,
) {
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
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
            isSearchEditable = isSearchEditable,
            modifier = Modifier
                .padding(
                    start = if (showUpNavigation) 0.dp else paddingLarge,
                    end = paddingLarge,
                    bottom = paddingSmall,
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
    isSearchEditable: Boolean,
    modifier: Modifier = Modifier,
    onOpenSearch: () -> Unit = {},
    onPerformSearch: (String) -> Unit = {},
    viewModel: GlobalSearchBarViewModel = hiltViewModel()
) {
    val textFieldShape = MaterialTheme.shapes.extraLarge
    val currentOnOpenSearch by rememberUpdatedState(onOpenSearch)

    val currentSearchText by viewModel.searchText.collectAsStateWithLifecycle()
    // Known bug: Non-empty initial text will overwrite user-entered text on rotation/config change.
    // NavGraph won't have restored the backstack in time. Initial text will default to "", then the
    // backstack + correct initial text comes in, which will update the VM and overwrite user text
    var shouldInitializeText by rememberSaveable(initialSearchText) { mutableStateOf(true) }
    LaunchedEffect(viewModel, initialSearchText) {
        if (shouldInitializeText) {
            shouldInitializeText = false
            viewModel.updateSearchText(
                TextFieldValue(
                    initialSearchText,
                    TextRange(initialSearchText.length)
                )
            )
        }
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
                value = currentSearchText,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(textFieldShape)
                    .border(0.5.dp, AmazonOutlineMedium, textFieldShape)
                    .focusable()
                    .focusRequester(focusRequester),
                onValueChange = { viewModel.updateSearchText(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onPerformSearch(currentSearchText.text) }),
                textStyle = MaterialTheme.typography.bodyLarge,
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
                                painter = painterResource(SharedR.drawable.ic_outline_search_24),
                                contentDescription = null
                            )
                        },
                        placeholder = {
                            if (currentSearchText.text.isEmpty()) {
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
private fun NavChipsRow(modifier: Modifier, navigationChips: List<String>) {
    val translucentWhite = Color(0x99FFFFFF)
    val navChipFont = MaterialTheme.typography.bodyLarge

    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val locationIconData = remember { createLocationIconData(navChipFont) }

    val navChipModifier = Modifier
        .clip(MaterialTheme.shapes.extraLarge)
        .background(translucentWhite)
        .padding(
            vertical = paddingSmall,
            horizontal = paddingMedium
        )

    Box(
        modifier = modifier
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = paddingLarge),
            horizontalArrangement = Arrangement.spacedBy(paddingSmall),
        ) {
            item {
                Text(
                    inlineContent = locationIconData.inlineContent,
                    modifier = navChipModifier,
                    style = navChipFont,
                    text = locationIconData.text,
                )
            }

            items(navigationChips) { item ->
                Text(
                    modifier = navChipModifier,
                    style = navChipFont,
                    text = item,
                )
            }
        }
    }
}

/**
 * Embeds the two icons (location + down arrow) in an [AnnotatedString] to
 * easily match the size of the other navigation chips, which are text-only.
 * This allows dynamic resizing for accessibility reasons. Eg, if the user
 * increases the system's font size from settings, this navigation chip
 * will resize the same as the text-only ones.
 *
 * The location icons will appear small while using Android's default
 * [Icons.Outlined] icons. This is because these icons have padding built
 * into the vector data, which takes up space. Once replaced with icons
 * having no built-in padding, it will look more like the Amazon app.
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
