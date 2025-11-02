package com.example.fakeamazon.features.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fakeamazon.R
import com.example.fakeamazon.app.ui.AMAZON_BEIGE
import com.example.fakeamazon.shared.ui.PriceDisplaySize
import com.example.fakeamazon.shared.ui.PriceText
import com.example.fakeamazon.shared.ui.PrimaryCta
import com.example.fakeamazon.shared.ui.WithPrimeLogoText
import com.example.fakeamazon.ui.theme.AmazonGray
import com.example.fakeamazon.ui.theme.AmazonOrange
import kotlinx.coroutines.delay
import kotlin.math.floor

private const val CART_ADDED_OVERLAY_TIMEOUT = 2000L

@Composable
fun ProductScreenRoot(
    modifier: Modifier = Modifier,
    productId: Int,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    LaunchedEffect(productId) {
        viewModel.load(productId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProductScreen(
        modifier = modifier,
        onAddToCart = { viewModel.addToCart() },
        onCartAddedViewed = { viewModel.onCartAddedViewed() },
        uiState = uiState,
    )
}

@Composable
private fun ProductScreen(
    modifier: Modifier = Modifier,
    onAddToCart: () -> Unit = {},
    onCartAddedViewed: () -> Unit = {},
    uiState: ProductUiState,
) {
    when (uiState) {
        is ProductUiState.Loading -> LoadingScreen(modifier)
        is ProductUiState.Error -> ErrorScreen(modifier)
        is ProductUiState.Loaded -> LoadedScreen(
            loadedState = uiState,
            modifier = modifier,
            onAddToCart = onAddToCart,
            onCartAddedViewed = onCartAddedViewed,
        )
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ErrorScreen(modifier: Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.error_loading_content))
        }
    }
}

@Composable
private fun LoadedScreen(
    loadedState: ProductUiState.Loaded,
    modifier: Modifier,
    onAddToCart: () -> Unit,
    onCartAddedViewed: () -> Unit,
) {
    val productInfo = loadedState.productInfo
    val addToCartState = loadedState.addToCartState
    val mainContentPadding = dimensionResource(R.dimen.main_content_padding_horizontal)

    Surface(modifier = modifier) {
        AddToCartBlockingOverlay(
            addToCartState = addToCartState,
            onCartAddedViewed = onCartAddedViewed,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = mainContentPadding, end = mainContentPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                StoreAndProductRatingHeader(
                    modifier = Modifier.fillMaxWidth(),
                    storeName = productInfo.storeName,
                    storeInitials = productInfo.storeInitials,
                    productRating = productInfo.productRating,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = productInfo.title,
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                ProductImage(
                    imageId = productInfo.imageId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )

                Spacer(modifier = Modifier.height(16.dp))

                PurchaseInfoView(
                    addToCartState = addToCartState,
                    modifier = Modifier.fillMaxWidth(),
                    onAddToCart = onAddToCart,
                    priceUSD = productInfo.priceUSD,
                )
            }
        }
    }
}

@Composable
private fun StoreAndProductRatingHeader(
    modifier: Modifier = Modifier,
    storeName: String,
    storeInitials: String,
    productRating: Float,
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AMAZON_BEIGE)
                .padding(6.dp)
        ) {
            BasicText(
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 14.sp,
                    maxFontSize = 22.sp,
                ),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.displayMedium,
                text = storeInitials,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = storeName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )

            Row {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Visit the Store",
                )

                val starCounts = calculateStarCounts(productRating)
                val starString = buildAnnotatedString {
                    append("$productRating ")
                    repeat(starCounts.fullStarCount) {
                        appendInlineContent("fullStar", "[*]")
                    }
                    repeat(starCounts.halfStarCount) {
                        appendInlineContent("halfStar", "[/]")
                    }
                    repeat(starCounts.emptyStarCount) {
                        appendInlineContent("emptyStar", "[-]")
                    }
                }

                Text(
                    inlineContent = starRatingsIconMap,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodySmall,
                    text = starString,
                )
            }
        }
    }
}

@Composable
fun ProductImage(modifier: Modifier, imageId: Int) {
    Box(modifier = modifier) {
        Image(
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(imageId),
        )
    }
}

@Composable
private fun PurchaseInfoView(
    addToCartState: AddToCartState,
    modifier: Modifier = Modifier,
    onAddToCart: () -> Unit,
    priceUSD: Float,
) {
    Column(modifier = modifier) {
        PriceText(
            displaySize = PriceDisplaySize.Large,
            modifier = Modifier,
            priceUSD = priceUSD,
        )

        WithPrimeLogoText {
            Text(
                fontWeight = FontWeight.Bold,
                inlineContent = it.inlineContent,
                text = buildAnnotatedString {
                    it.appendPrimeLogo(this)
                    append(" Overnight")
                },
            )
        }

        val primaryCtaText = if (addToCartState != AddToCartState.Adding) {
            stringResource(R.string.add_to_cart)
        } else {
            "..."
        }

        PrimaryCta(
            enabled = addToCartState != AddToCartState.Adding,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAddToCart() },
            text = primaryCtaText,
        )
    }
}

@Composable
private fun AddToCartBlockingOverlay(
    addToCartState: AddToCartState,
    onCartAddedViewed: () -> Unit
) {
    if (addToCartState == AddToCartState.Inactive) {
        return
    }

    InteractionBlockingOverlay(modifier = Modifier.fillMaxWidth()) {
        if (addToCartState == AddToCartState.Added) {
            Text(
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = AmazonGray,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                text = "Added to cart",
            )

            LaunchedEffect(Unit) {
                delay(CART_ADDED_OVERLAY_TIMEOUT)
                onCartAddedViewed()
            }
        }
    }
}

@Composable
fun InteractionBlockingOverlay(modifier: Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .zIndex(1f)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent(pass = PointerEventPass.Initial)
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

private fun calculateStarCounts(rawProductRating: Float): StarCounts {
    val productRating = rawProductRating.coerceAtMost(5f)
    val ratingFraction = productRating - floor(productRating)

    var fullStarCount = productRating.toInt()
    var halfStarCount = 0

    when {
        ratingFraction >= 0.8f -> fullStarCount++ // .8+ = round up to full-star
        ratingFraction >= 0.3f -> halfStarCount = 1 // .3+ = show half-star
        else -> {} // do nothing
    }

    val emptyStarCount = 5 - fullStarCount - halfStarCount // fill remaining with empty-stars

    return StarCounts(fullStarCount, halfStarCount, emptyStarCount)
}

private val starRatingsIconMap = mapOf(
    "fullStar" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.Center)) {
        Icon(
            contentDescription = null,
            painter = painterResource(R.drawable.ic_baseline_star_24),
            tint = AmazonOrange,
        )
    },
    "halfStar" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.Center)) {
        Icon(
            contentDescription = null,
            painter = painterResource(R.drawable.ic_outline_star_half_24),
            tint = AmazonOrange,
        )
    },
    "emptyStar" to InlineTextContent(Placeholder(1.em, 1.em, PlaceholderVerticalAlign.Center)) {
        Icon(
            contentDescription = null,
            painter = painterResource(R.drawable.ic_outline_star_24),
            tint = AmazonOrange,
        )
    },
)

data class StarCounts(
    val fullStarCount: Int,
    val halfStarCount: Int,
    val emptyStarCount: Int,
)
