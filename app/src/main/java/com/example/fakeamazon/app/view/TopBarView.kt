package com.example.fakeamazon.app.view

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.dimensionResource
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
import com.example.fakeamazon.R
import com.example.fakeamazon.ui.theme.AmazonOutlineMedium

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
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingLarge = dimensionResource(R.dimen.padding_large)

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

        Column(modifier = Modifier.padding(bottom = paddingMedium)) {
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
                navigationChips = NAV_CHIPS,
            )
        }
    }
}

@Composable
fun AmazonTopAppBar(
    modifier: Modifier = Modifier,
) {
    val paddingLarge = dimensionResource(R.dimen.padding_large)
    val paddingSmall = dimensionResource(R.dimen.padding_small)

    Box(modifier = modifier) {
        SimpleSearchBar(
            modifier = Modifier
                .padding(
                    start = paddingLarge,
                    end = paddingLarge,
                    bottom = paddingSmall,
                )
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SimpleSearchBar(modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val textFieldShape = MaterialTheme.shapes.extraLarge

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
                    .height(44.dp)
                    .clip(textFieldShape)
                    .border(0.5.dp, AmazonOutlineMedium, textFieldShape),
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
            Icon(Icons.Outlined.LocationOn, null)
        },
        downIconId to InlineTextContent(placeholder = placeholder) {
            Icon(Icons.Outlined.KeyboardArrowDown, null)
        }
    )

    return LocationIconData(locationIconString, inlineContent)
}

private data class LocationIconData(
    val text: AnnotatedString,
    val inlineContent: Map<String, InlineTextContent>,
)
