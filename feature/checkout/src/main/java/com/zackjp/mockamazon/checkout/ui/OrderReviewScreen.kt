package com.zackjp.mockamazon.checkout.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zackjp.mockamazon.checkout.R
import com.zackjp.mockamazon.shared.ui.PrimaryCta
import com.zackjp.mockamazon.shared.ui.text.appendLink


@Composable
fun OrderReviewScreenRoot(
    modifier: Modifier = Modifier,
) {
    val orderInfo = OrderInfo(
        fullAddress = "123 Example St., Apt. 867, New York, NY 10101, United States",
        fullName = "John Doe",
        paymentMethodText = "Visa 1234",
    )

    val mainContentPadding = dimensionResource(R.dimen.main_content_padding_horizontal)

    Surface(
        modifier = modifier
            .padding(horizontal = mainContentPadding, vertical = 16.dp),
    ) {
        LazyColumn {
            item {
                TosAndPlaceOrderCta(modifier = Modifier.fillMaxWidth())
            }

            itemSectionWithDivider {
                OrderCostSummary(modifier = Modifier.fillMaxWidth())
            }

            itemSectionWithDivider {
                PaymentMethodOverview(
                    modifier = Modifier.fillMaxWidth(),
                    orderInfo = orderInfo,
                )
            }

            itemSectionWithDivider {
                DeliveryOverview(
                    modifier = Modifier.fillMaxWidth(),
                    orderInfo = orderInfo,
                )
            }
        }
    }
}

private fun LazyListScope.itemSectionWithDivider(section: @Composable () -> Unit) {
    item {
        Column {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            section()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TosAndPlaceOrderCta(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(R.string.checkout_order_review_tos_label)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryCta(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = stringResource(R.string.checkout_order_review_place_order_cta),
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun OrderCostSummary(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        OrderCostLineItem(
            label = stringResource(R.string.checkout_subtotal_label),
            value = "$-",
        )
        OrderCostLineItem(
            label = stringResource(R.string.checkout_shipping_and_handling_label),
            value = "$-",
        )
        OrderCostLineItem(
            label = stringResource(R.string.checkout_estimated_tax_label),
            value = "$-",
        )
        OrderCostLineItem(
            label = stringResource(R.string.checkout_order_total_label),
            style = MaterialTheme.typography.labelLarge,
            value = "$-",
        )
    }
}

@Composable
private fun OrderCostLineItem(
    label: String,
    style: TextStyle? = null,
    value: String,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalTextStyle provides (style ?: LocalTextStyle.current)) {
            Text(
                modifier = Modifier.weight(1f),
                text = label,
            )
            Text(
                modifier = Modifier,
                text = value,
            )
        }
    }
}

@Composable
private fun PaymentMethodOverview(
    modifier: Modifier = Modifier,
    orderInfo: OrderInfo,
) {
    Column(modifier = modifier) {
        Text(
            style = MaterialTheme.typography.labelLarge,
            text = stringResource(R.string.checkout_paying_with, orderInfo.paymentMethodText),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_change_payment_method))
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_use_alternate_payments))
            },
        )
    }
}

@Composable
private fun DeliveryOverview(
    modifier: Modifier = Modifier,
    orderInfo: OrderInfo,
) {
    Column(modifier = modifier) {
        val verticalSpacing = 16.dp

        Text(
            style = MaterialTheme.typography.labelLarge,
            text = stringResource(R.string.checkout_deliver_to, orderInfo.fullName),
        )

        Text(
            text = orderInfo.fullAddress,
        )

        Spacer(modifier = Modifier.height(verticalSpacing))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_change_delivery_address))
            },
        )

        Spacer(modifier = Modifier.height(verticalSpacing))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_add_delivery_instructions))
            },
        )

        Spacer(modifier = Modifier.height(verticalSpacing))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_free_pickup_near_address))
            },
        )
    }
}

@Preview
@Composable
private fun OrderReviewPreview() {
    Surface {
        OrderReviewScreenRoot(modifier = Modifier)
    }
}

private data class OrderInfo(
    val fullAddress: String,
    val fullName: String,
    val paymentMethodText: String,
)
