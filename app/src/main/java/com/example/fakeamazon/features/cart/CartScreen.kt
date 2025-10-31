package com.example.fakeamazon.features.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fakeamazon.R
import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.ui.PrimaryCta
import com.example.fakeamazon.ui.theme.Gray90
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val CART_CONTAINER_COLOR = Gray90
private val DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.getDefault())

@Composable
fun CartScreenRoot(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel<CartViewModel>(),
    onViewProduct: (Int) -> Unit,
) {
    LaunchedEffect(Unit) {
        cartViewModel.load()
    }

    val cartItemsState = cartViewModel.cartItems.collectAsStateWithLifecycle()

    CartScreen(
        modifier = modifier,
        cartItems = cartItemsState.value,
        onViewProduct = onViewProduct,
    )
}

@Composable
private fun CartScreen(
    cartItems: List<CartItem>,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit = {},
) {
    if (cartItems.isEmpty()) {
        Surface(modifier = modifier) {}
    } else {
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
                        onViewProduct = onViewProduct,
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
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
    onViewProduct: (Int) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CART_CONTAINER_COLOR),
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                LeftPanel(
                    imagePainter = painterResource(cartItem.imageId),
                    modifier = Modifier.width(132.dp),
                    onViewProduct = onViewProduct,
                    productId = cartItem.id,
                )

                Spacer(modifier = Modifier.width(10.dp))

                RightPanel(
                    cartItem = cartItem,
                    modifier = Modifier.weight(1f),
                    onViewProduct = onViewProduct,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CartItemQuantityChip(cartItem.quantity)
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
        ProductPriceText(
            modifier = Modifier.fillMaxWidth(),
            priceUSD = cartItem.priceUSD,
        )

        Spacer(modifier = Modifier.height(2.dp))
        PrimeDayText(extraLineHeightTextStyle)
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
private fun ProductPriceText(modifier: Modifier = Modifier, priceUSD: Float) {
    Text(
        maxLines = 1,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 19.sp),
        text = "$$priceUSD",
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
            val deliveryText = stringResource(R.string.cart_item_free_delivery)
            val deliveryDate = DATE_FORMATTER.format(estDeliveryDate)
            append("$deliveryText ")
            addStyle(
                SpanStyle(fontWeight = FontWeight.Bold),
                length,
                length + deliveryDate.length
            )
            append(deliveryDate)
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
private fun PrimeDayText(extraLineHeightTextStyle: TextStyle, modifier: Modifier = Modifier) {
    Text(
        maxLines = 1,
        modifier = modifier,
        style = extraLineHeightTextStyle.copy(fontWeight = FontWeight.Bold),
        text = buildAnnotatedString {
            append("Prime Two-Day")
            addStyle(
                style = SpanStyle(
                    color = Color(0xFF3779F6),
                ),
                start = 0,
                end = 5
            )
        },
    )
}

@Composable
private fun InStockText(extraLineHeightTextStyle: TextStyle, modifier: Modifier = Modifier) {
    Text(
        color = Color(0xFF347840),
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
                2.dp,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.large
            )
            .padding(6.dp)
            .width(108.dp),
        style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
        text = "$quantity",
        textAlign = TextAlign.Center,
    )
}
