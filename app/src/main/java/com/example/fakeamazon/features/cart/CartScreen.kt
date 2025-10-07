package com.example.fakeamazon.features.cart

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.fakeamazon.R
import com.example.fakeamazon.ui.theme.Gray90
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

val DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.getDefault())

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onViewProduct: () -> Unit,
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            CartListHeader(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.main_content_padding_horizontal))
                    .fillMaxWidth()
            )

            val cartItem = CartItem(
                imageId = R.drawable.item_game_lost_cities,
                title = "Lost Cities Card Game - with 6th Expedition - Thames & Kosmos Store - Designed By Reiner Knizia",
                priceUSD = 19.99f,
                deliveryCostUSD = 0f,
                estDeliveryDate = LocalDate.now().plusDays(2),
                isInStock = true,
            )

            CartItem(
                cartItem = cartItem,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                onViewProduct = onViewProduct,
            )
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

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                text = stringResource(R.string.cart_proceed_to_checkout),
                style = MaterialTheme.typography.labelLarge,
            )
        }

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
    onViewProduct: () -> Unit,
) {
    val containerColor = Gray90
    val mainTextStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 1.25.em)

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .size(132.dp)
                        .clickable { onViewProduct() },
                    colorFilter = ColorFilter.tint(
                        blendMode = BlendMode.Multiply,
                        color = containerColor
                    ),
                    contentDescription = null,
                    painter = painterResource(cartItem.imageId),
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewProduct() },
                        overflow = TextOverflow.Ellipsis,
                        style = mainTextStyle,
                        text = cartItem.title,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 19.sp),
                        text = "$${cartItem.priceUSD}",
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        style = mainTextStyle.copy(fontWeight = FontWeight.Bold),
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

                    Text(
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        style = mainTextStyle,
                        text = buildAnnotatedString {
                            val deliveryText = stringResource(R.string.cart_item_free_delivery)
                            val deliveryDate = DATE_FORMATTER.format(cartItem.estDeliveryDate)
                            append("$deliveryText ")
                            addStyle(
                                SpanStyle(fontWeight = FontWeight.Bold),
                                length,
                                length + deliveryDate.length
                            )
                            append(deliveryDate)
                        },
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        color = Color(0xFF245CD5),
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        style = mainTextStyle,
                        text = stringResource(R.string.cart_item_free_returns),
                    )

                    if (cartItem.isInStock) {
                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            color = Color(0xFF347840),
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(),
                            style = mainTextStyle,
                            text = stringResource(R.string.cart_item_in_stock),
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                }
            }
        }
    }
}

private data class CartItem(
    @param:DrawableRes val imageId: Int,
    val title: String,
    val priceUSD: Float, // we'll want this in local currency, but for now indicate it's USD only
    val deliveryCostUSD: Float,
    val estDeliveryDate: LocalDate,
    val isInStock: Boolean,
)
