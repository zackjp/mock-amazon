package com.example.mockamazon.features.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mockamazon.R
import com.example.mockamazon.shared.model.CartItem
import com.example.mockamazon.shared.theme.AmazonOutlineMedium
import com.example.mockamazon.shared.theme.Gray90
import com.example.mockamazon.shared.theme.Green60
import com.example.mockamazon.shared.ui.CartItemQuantityChip
import com.example.mockamazon.shared.ui.InteractionBlockingOverlay
import com.example.mockamazon.shared.ui.PriceDisplaySize
import com.example.mockamazon.shared.ui.PriceText
import com.example.mockamazon.shared.ui.PrimaryCta
import com.example.mockamazon.shared.ui.component.ExpectedDeliveryText
import com.example.mockamazon.shared.ui.component.PrimeDayText
import com.example.mockamazon.shared.ui.screen.ErrorScreen
import com.example.mockamazon.shared.ui.screen.LoadingScreen

private val CART_CONTAINER_COLOR = Gray90

@Composable
fun CartScreenRoot(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel<CartViewModel>(),
    onViewProduct: (Int) -> Unit,
) {
    LaunchedEffect(cartViewModel) {
        cartViewModel.load()
    }

    val screenState = cartViewModel.screenState.collectAsStateWithLifecycle()

    CartScreen(
        modifier = modifier,
        onDecrementCartItem = { productId -> cartViewModel.decrementCartItem(productId) },
        onIncrementCartItem = { productId -> cartViewModel.incrementCartItem(productId) },
        onRemoveCartItem = { productId -> cartViewModel.removeByProductId(productId) },
        onViewProduct = onViewProduct,
        screenState = screenState.value,
    )
}

@Composable
private fun CartScreen(
    modifier: Modifier = Modifier,
    onDecrementCartItem: (Int) -> Unit,
    onIncrementCartItem: (Int) -> Unit,
    onRemoveCartItem: (Int) -> Unit,
    onViewProduct: (Int) -> Unit = {},
    screenState: CartScreenState,
) {
    when (screenState) {
        is CartScreenState.Loading -> LoadingScreen(modifier)
        is CartScreenState.Loaded -> LoadedView(
            modifier = modifier,
            onDecrementCartItem = onDecrementCartItem,
            onIncrementCartItem = onIncrementCartItem,
            onRemoveCartItem = onRemoveCartItem,
            onViewProduct = onViewProduct,
            screenState = screenState,
        )

        is CartScreenState.Error -> ErrorScreen(modifier)
    }
}

@Composable
private fun LoadedView(
    modifier: Modifier = Modifier,
    onDecrementCartItem: (Int) -> Unit,
    onIncrementCartItem: (Int) -> Unit,
    onRemoveCartItem: (Int) -> Unit,
    onViewProduct: (Int) -> Unit,
    screenState: CartScreenState.Loaded,
) {
    val totalPriceUSD = screenState.totalPriceUSD
    val cartItems = screenState.cartItems
    val paddingSmall = dimensionResource(R.dimen.padding_small)

    Surface(modifier = modifier) {
        val isReloading = screenState.isReloading
        if (isReloading) {
            InteractionBlockingOverlay(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator()
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            item {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                CartListHeader(
                    itemCount = cartItems.sumOf { it.quantity },
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.main_content_padding_horizontal))
                        .fillMaxWidth(),
                    totalPriceUSD = totalPriceUSD,
                )
            }

            items(cartItems, key = { cartItem -> cartItem.id }) { cartItem ->
                CartItem(
                    cartItem = cartItem,
                    modifier = Modifier
                        .padding(horizontal = paddingSmall)
                        .fillMaxWidth(),
                    onDecrementCartItem = onDecrementCartItem,
                    onIncrementCartItem = onIncrementCartItem,
                    onRemoveCartItem = onRemoveCartItem,
                    onViewProduct = onViewProduct,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun CartListHeader(
    itemCount: Int,
    modifier: Modifier = Modifier,
    totalPriceUSD: Float,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.cart_title),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp)
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 12.dp)
        )

        Text(
            text = stringResource(R.string.cart_subtotal, totalPriceUSD),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryCta(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = pluralStringResource(R.plurals.cart_proceed_to_checkout, itemCount, itemCount),
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )
    }
}

@Composable
private fun CartItem(
    cartItem: CartItem,
    modifier: Modifier = Modifier,
    onDecrementCartItem: (Int) -> Unit,
    onIncrementCartItem: (Int) -> Unit,
    onRemoveCartItem: (Int) -> Unit,
    onViewProduct: (Int) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CART_CONTAINER_COLOR),
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (leftPanel, rightPanel, quantityChip, deleteButton) = createRefs()

                LeftPanel(
                    imagePainter = painterResource(cartItem.imageId),
                    modifier = Modifier
                        .constrainAs(leftPanel) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                        .width(132.dp),
                    onViewProduct = onViewProduct,
                    productId = cartItem.id,
                )

                RightPanel(
                    cartItem = cartItem,
                    modifier = Modifier.constrainAs(rightPanel) {
                        start.linkTo(leftPanel.end, 10.dp)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    },
                    onViewProduct = onViewProduct,
                )

                val barrier = createBottomBarrier(leftPanel, rightPanel, margin = 8.dp)

                CartItemQuantityChip(
                    modifier = Modifier
                        .width(116.dp)
                        .constrainAs(quantityChip) {
                            top.linkTo(barrier)
                            start.linkTo(leftPanel.start)
                            end.linkTo(leftPanel.end)
                            horizontalBias = 0f
                        },
                    onDecrement = { onDecrementCartItem(cartItem.id) },
                    onIncrement = { onIncrementCartItem(cartItem.id) },
                    quantity = cartItem.quantity,
                )

                CartActionButton(
                    onClick = { onRemoveCartItem(cartItem.id) },
                    modifier = Modifier.constrainAs(deleteButton) {
                        start.linkTo(rightPanel.start)
                        top.linkTo(barrier)
                    },
                    text = stringResource(R.string.delete),
                )
            }
        }
    }
}

@Composable
private fun LeftPanel(
    imagePainter: Painter,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
    productId: Int,
) {
    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewProduct(productId) },
            colorFilter = ColorFilter.tint(
                blendMode = BlendMode.Multiply,
                color = CART_CONTAINER_COLOR
            ),
            contentDescription = null,
            painter = imagePainter,
        )
    }
}

@Composable
private fun RightPanel(
    cartItem: CartItem,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
) {
    val extraLineHeightTextStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 1.25.em)
    Column(modifier = modifier) {
        ProductTitle(
            extraLineHeightTextStyle = extraLineHeightTextStyle,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewProduct(cartItem.id) },
            title = cartItem.title,
        )

        Spacer(modifier = Modifier.height(4.dp))
        PriceText(
            displaySize = PriceDisplaySize.Medium,
            modifier = Modifier,
            priceUSD = cartItem.priceUSD,
        )

        Spacer(modifier = Modifier.height(2.dp))
        PrimeDayText(
            style = extraLineHeightTextStyle,
            estDeliveryDate = cartItem.estDeliveryDate,
        )
        ExpectedDeliveryText(
            style = extraLineHeightTextStyle,
            estDeliveryDate = cartItem.estDeliveryDate,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(2.dp))
        ReturnPolicyText(extraLineHeightTextStyle, modifier = Modifier.fillMaxWidth())

        if (cartItem.isInStock) {
            Spacer(modifier = Modifier.height(2.dp))
            InStockText(extraLineHeightTextStyle, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Composable
private fun ProductTitle(
    extraLineHeightTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        maxLines = 2,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        style = extraLineHeightTextStyle,
        text = title,
    )
}

@Composable
private fun ReturnPolicyText(extraLineHeightTextStyle: TextStyle, modifier: Modifier = Modifier) {
    Text(
        color = Color(0xFF245CD5),
        maxLines = 1,
        modifier = modifier,
        style = extraLineHeightTextStyle,
        text = stringResource(R.string.cart_item_free_returns),
    )
}

@Composable
private fun InStockText(extraLineHeightTextStyle: TextStyle, modifier: Modifier = Modifier) {
    Text(
        color = Green60,
        maxLines = 1,
        modifier = modifier,
        style = extraLineHeightTextStyle,
        text = stringResource(R.string.cart_item_in_stock),
    )
}

@Composable
private fun CartActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(Color.White, MaterialTheme.shapes.large)
            .border(
                width = 0.5.dp,
                color = AmazonOutlineMedium,
                shape = MaterialTheme.shapes.large
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        style = MaterialTheme.typography.bodyMedium,
        text = text,
        textAlign = TextAlign.Center,
    )
}
