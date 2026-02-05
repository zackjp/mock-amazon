package com.zackjp.mockamazon.features.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zackjp.mockamazon.shared.model.ProductInfo
import com.zackjp.mockamazon.shared.theme.AmazonOutlineLight
import com.zackjp.mockamazon.shared.theme.Gray90
import com.zackjp.mockamazon.shared.ui.CartItemQuantityChip
import com.zackjp.mockamazon.shared.ui.PriceDisplaySize
import com.zackjp.mockamazon.shared.ui.PriceText
import com.zackjp.mockamazon.shared.ui.PrimaryCta
import com.zackjp.mockamazon.shared.ui.component.ExpectedDeliveryText
import com.zackjp.mockamazon.shared.ui.component.PrimeDayText
import com.zackjp.mockamazon.shared.ui.getRatingStarsTextInfo
import com.zackjp.mockamazon.shared.ui.screen.ErrorScreen
import com.zackjp.mockamazon.shared.ui.screen.LoadingScreen
import org.orbitmvi.orbit.compose.collectAsState
import com.zackjp.mockamazon.shared.R as SharedR

@Composable
fun SearchResultsScreenRoot(
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
    searchString: String,
    viewModel: SearchResultsViewModel = hiltViewModel<SearchResultsViewModel>(),
) {

    LaunchedEffect(viewModel, searchString) {
        viewModel.load(searchString)
    }

    val screenState = viewModel.collectAsState().value

    SearchResultsScreen(
        modifier = modifier,
        onAddToCart = { productId -> viewModel.addToCart(productId) },
        onDecrementFromCart = { productId -> viewModel.decrementFromCart(productId) },
        onViewProduct = onViewProduct,
        screenState = screenState,
    )
}

@Composable
private fun SearchResultsScreen(
    modifier: Modifier = Modifier,
    onAddToCart: (Int) -> Unit,
    onDecrementFromCart: (Int) -> Unit,
    onViewProduct: (Int) -> Unit,
    screenState: SearchResultsScreenState,
) {
    when (screenState) {
        is SearchResultsScreenState.Loaded -> LoadedView(
            loadedState = screenState,
            modifier = modifier,
            onAddToCart = onAddToCart,
            onDecrementFromCart = onDecrementFromCart,
            onViewProduct = onViewProduct,
        )
        is SearchResultsScreenState.Loading -> LoadingScreen(modifier = modifier)
        is SearchResultsScreenState.Error -> ErrorScreen(modifier)
    }
}

@Composable
private fun LoadedView(
    loadedState: SearchResultsScreenState.Loaded,
    modifier: Modifier,
    onAddToCart: (Int) -> Unit,
    onDecrementFromCart: (Int) -> Unit,
    onViewProduct: (Int) -> Unit,
) {
    val contentPadding = dimensionResource(SharedR.dimen.padding_small)
    val cartCounts = loadedState.requestedCartCounts

    Surface(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = contentPadding),
            modifier = Modifier.padding(horizontal = contentPadding),
        ) {
            items(loadedState.searchResults) { productInfo ->
                val cartCount = cartCounts[productInfo.id] ?: 0
                SearchResultCard(
                    modifier = Modifier.fillMaxWidth(),
                    onAddToCart = onAddToCart,
                    onDecrementFromCart = onDecrementFromCart,
                    onViewProduct = onViewProduct,
                    productInfo = productInfo,
                    cartCount = cartCount,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SearchResultCard(
    cartCount: Int,
    modifier: Modifier = Modifier,
    onAddToCart: (productId: Int) -> Unit,
    onDecrementFromCart: (Int) -> Unit,
    onViewProduct: (Int) -> Unit,
    productInfo: ProductInfo,
) {
    val cardShape = MaterialTheme.shapes.extraSmall
    val cardContentPadding = 8.dp
    val imageBackgroundColor = Gray90

    Card(
        shape = cardShape,
        modifier = modifier
            .clickable { onViewProduct(productInfo.id) }
            .border(Dp.Hairline, AmazonOutlineLight, cardShape),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (leftPanel, rightPanel) = createRefs()
            val leftPanelEnd = createGuidelineFromStart(0.4f)

            Column(
                modifier = Modifier
                    .background(imageBackgroundColor)
                    .constrainAs(leftPanel) {
                        start.linkTo(parent.start)
                        top.linkTo(rightPanel.top)
                        end.linkTo(leftPanelEnd)
                        bottom.linkTo(rightPanel.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(cardContentPadding),
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    colorFilter = ColorFilter.tint(imageBackgroundColor, BlendMode.Multiply),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(productInfo.imageId),
                )
            }

            Column(
                modifier = Modifier
                    .constrainAs(rightPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(leftPanel.end)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
                    .padding(cardContentPadding)
            ) {
                val textStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 1.35.em)
                CompositionLocalProvider(LocalTextStyle provides textStyle) {
                    Text(
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        text = productInfo.title,
                    )

                    val ratingStarsTextInfo = getRatingStarsTextInfo(productInfo.productRating)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        inlineContent = ratingStarsTextInfo.inlineContent,
                        text = buildAnnotatedString {
                            append("${ratingStarsTextInfo.normalizedRating} ")
                            append(ratingStarsTextInfo.text)
                        },
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                    PriceText(modifier = Modifier, productInfo.priceUSD, PriceDisplaySize.SP22)

                    Spacer(modifier = Modifier.height(4.dp))
                    val estDeliveryDate = productInfo.deliveryDate
                    PrimeDayText(
                        estDeliveryDate = estDeliveryDate,
                        modifier = Modifier,
                        style = LocalTextStyle.current,
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    ExpectedDeliveryText(
                        estDeliveryDate = estDeliveryDate,
                        modifier = Modifier,
                        style = LocalTextStyle.current,
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    val cartInteractorModifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                    if (cartCount <= 0) {
                        PrimaryCta(
                            modifier = cartInteractorModifier,
                            onClick = { onAddToCart(productInfo.id) },
                            text = stringResource(SharedR.string.add_to_cart),
                        )
                    } else {
                        CartItemQuantityChip(
                            modifier = cartInteractorModifier,
                            quantity = cartCount,
                            onDecrement = { onDecrementFromCart(productInfo.id) },
                            onIncrement = { onAddToCart(productInfo.id) },
                        )
                    }
                }
            }
        }
    }

}
