package com.example.fakeamazon.features.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fakeamazon.R
import com.example.fakeamazon.features.product.view.ProductImagesView
import com.example.fakeamazon.features.product.view.PurchaseInfoView
import com.example.fakeamazon.features.product.view.SimilarProductsView
import com.example.fakeamazon.features.product.view.StoreNameAndProductRatingView
import com.example.fakeamazon.shared.ui.InteractionBlockingOverlay
import com.example.fakeamazon.ui.theme.AmazonGray
import kotlinx.coroutines.delay

private const val CART_ADDED_OVERLAY_TIMEOUT = 2000L

@Composable
fun ProductScreenRoot(
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
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
        onViewProduct = onViewProduct,
        uiState = uiState,
    )
}

@Composable
private fun ProductScreen(
    modifier: Modifier = Modifier,
    onAddToCart: () -> Unit = {},
    onCartAddedViewed: () -> Unit = {},
    onViewProduct: (Int) -> Unit = {},
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
            onViewProduct = onViewProduct,
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
    onViewProduct: (Int) -> Unit,
) {
    val productInfo = loadedState.productInfo
    val similarProducts = loadedState.similarProducts
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
                .padding(start = mainContentPadding, end = mainContentPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                StoreNameAndProductRatingView(
                    modifier = Modifier.fillMaxWidth(),
                    storeName = productInfo.storeName,
                    storeInitials = productInfo.storeInitials,
                    productRating = productInfo.productRating,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(productInfo.title)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                ProductImagesView(
                    mainContentPadding = mainContentPadding,
                    imageId = productInfo.imageId,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                PurchaseInfoView(
                    addToCartState = addToCartState,
                    deliveryDate = productInfo.deliveryDate,
                    modifier = Modifier.fillMaxWidth(),
                    onAddToCart = onAddToCart,
                    priceUSD = productInfo.priceUSD,
                )
            }

            item {
                if (similarProducts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))

                    SimilarProductsView(
                        horizontalContentPadding = mainContentPadding,
                        modifier = Modifier.fillMaxWidth(),
                        onViewProduct = onViewProduct,
                        similarProducts = similarProducts,
                    )
                }
            }

            item {
                // Add bottom spacing
                Spacer(Modifier.height(32.dp))
            }
        }
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
