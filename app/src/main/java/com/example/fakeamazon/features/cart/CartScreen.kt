package com.example.fakeamazon.features.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fakeamazon.R
import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.toPrimeDeliveryString
import com.example.fakeamazon.shared.toRelativeDateString
import com.example.fakeamazon.shared.ui.PriceDisplaySize
import com.example.fakeamazon.shared.ui.PriceText
import com.example.fakeamazon.shared.ui.PrimaryCta
import com.example.fakeamazon.shared.ui.WithPrimeLogoText
import com.example.fakeamazon.ui.theme.AmazonOutlineMedium
import com.example.fakeamazon.ui.theme.Gray90
import com.example.fakeamazon.ui.theme.Green60
import java.time.LocalDate

private val CART_CONTAINER_COLOR = Gray90

@Composable
fun CartScreenRoot(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel<CartViewModel>(),
    onViewProduct: (Int) -> Unit,
) {
    LaunchedEffect(Unit) {
        cartViewModel.load()
    }

    val screenState = cartViewModel.screenState.collectAsStateWithLifecycle()

    CartScreen(
        modifier = modifier,
        onRemoveCartItem = { productId -> cartViewModel.removeByProductId(productId) },
        onViewProduct = onViewProduct,
        screenState = screenState.value,
    )
}

@Composable
private fun CartScreen(
    modifier: Modifier = Modifier,
    onRemoveCartItem: (Int) -> Unit,
    onViewProduct: (Int) -> Unit = {},
    screenState: CartScreenState,
) {
    when (screenState) {
        is CartScreenState.Loading -> LoadingView(modifier)
        is CartScreenState.Loaded -> LoadedView(
            modifier = modifier,
            onRemoveCartItem = onRemoveCartItem,
            onViewProduct = onViewProduct,
            screenState = screenState,
        )

        is CartScreenState.Error -> ErrorView(modifier)
    }
}

@Composable
private fun LoadingView(modifier: Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ErrorView(modifier: Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.error_loading_content))
        }
    }
}

@Composable
private fun LoadedView(
    modifier: Modifier = Modifier,
    onRemoveCartItem: (Int) -> Unit,
    onViewProduct: (Int) -> Unit,
    screenState: CartScreenState.Loaded,
) {
    val cartItems = screenState.cartItems
    Surface(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            item {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                CartListHeader(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.main_content_padding_horizontal))
                        .fillMaxWidth()
                )
            }

            items(cartItems) { cartItem ->
                CartItem(
                    cartItem = cartItem,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    onRemoveCartItem = onRemoveCartItem,
                    onViewProduct = onViewProduct,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun CartListHeader(modifier: Modifier = Modifier) {
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
            text = stringResource(R.string.cart_subtotal, "0"),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryCta(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = stringResource(R.string.cart_proceed_to_checkout),
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
            extraLineHeightTextStyle = extraLineHeightTextStyle,
            estDeliveryDate = cartItem.estDeliveryDate,
        )
        ExpectedDeliveryText(
            extraLineHeightTextStyle = extraLineHeightTextStyle,
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
private fun ExpectedDeliveryText(
    extraLineHeightTextStyle: TextStyle,
    estDeliveryDate: LocalDate,
    modifier: Modifier = Modifier,
) {
    Text(
        maxLines = 1,
        modifier = modifier,
        style = extraLineHeightTextStyle,
        text = buildAnnotatedString {
            val deliveryDate = estDeliveryDate.toRelativeDateString(LocalContext.current, false)
            append(stringResource(R.string.cart_item_free_delivery) + " ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(deliveryDate)
            }
        },
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
private fun PrimeDayText(
    extraLineHeightTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    estDeliveryDate: LocalDate
) {
    WithPrimeLogoText {
        Text(
            inlineContent = it.inlineContent,
            maxLines = 1,
            modifier = modifier,
            style = extraLineHeightTextStyle.copy(fontWeight = FontWeight.Bold),
            text = buildAnnotatedString {
                it.appendPrimeLogo(this)
                val primeDeliveryText = estDeliveryDate.toPrimeDeliveryString(LocalContext.current)
                primeDeliveryText?.let { primeDeliveryText ->
                    append(" $primeDeliveryText")
                }
            },
        )
    }
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
private fun CartItemQuantityChip(quantity: Int, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .background(Color.White, MaterialTheme.shapes.large)
            .border(
                width = 2.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
        text = "$quantity",
        textAlign = TextAlign.Center,
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
