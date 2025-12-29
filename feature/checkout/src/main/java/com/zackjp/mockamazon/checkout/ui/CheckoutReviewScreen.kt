package com.zackjp.mockamazon.checkout.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zackjp.mockamazon.checkout.R
import com.zackjp.mockamazon.checkout.ui.model.CheckoutState
import com.zackjp.mockamazon.shared.model.Cart
import com.zackjp.mockamazon.shared.model.User
import com.zackjp.mockamazon.shared.theme.LinkBlue
import com.zackjp.mockamazon.shared.ui.PrimaryCta
import com.zackjp.mockamazon.shared.ui.screen.ErrorScreen
import com.zackjp.mockamazon.shared.ui.screen.LoadingScreen
import com.zackjp.mockamazon.shared.ui.text.appendLink
import org.orbitmvi.orbit.compose.collectAsState


@Composable
fun CheckoutReviewScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CheckoutReviewViewModel = hiltViewModel(),
) {
    LaunchedEffect(viewModel) {
        viewModel.load()
    }

    val state = viewModel.collectAsState().value

    CheckoutReviewScreen(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun CheckoutReviewScreen(
    modifier: Modifier = Modifier,
    state: CheckoutState,
) {
    when (state) {
        is CheckoutState.Error -> ErrorScreen(modifier)
        is CheckoutState.Loaded -> Loaded(
            modifier = modifier,
            state = state,
        )
        is CheckoutState.Loading -> LoadingScreen(modifier)
    }
}

@Composable
private fun Loaded(
    modifier: Modifier,
    state: CheckoutState.Loaded,
) {
    val mainContentPadding = dimensionResource(R.dimen.main_content_padding_horizontal)

    Surface(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = mainContentPadding, vertical = 16.dp)
        ) {
            item {
                TosAndPlaceOrderCta(modifier = Modifier.fillMaxWidth())
            }

            itemSectionWithDivider {
                OrderCostSummary(
                    cart = state.cart,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            itemSectionWithDivider {
                PaymentMethodOverview(
                    modifier = Modifier.fillMaxWidth(),
                    user = state.user,
                )
            }

            itemSectionWithDivider {
                DeliveryOverview(
                    modifier = Modifier.fillMaxWidth(),
                    user = state.user,
                )
            }

            itemSectionWithDivider {
                PlaceOrderCtaAndFinalTos(
                    modifier = Modifier.fillMaxWidth(),
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
            text = stringResource(R.string.checkout_review_tos_label)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryCta(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = stringResource(R.string.checkout_review_place_order_cta),
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun OrderCostSummary(
    cart: Cart,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        OrderCostLineItem(
            label = stringResource(R.string.checkout_review_order_subtotal_label),
            value = "$${cart.totalPriceUSD}",
        )
        OrderCostLineItem(
            label = stringResource(R.string.checkout_review_shipping_and_handling_label),
            value = "$-",
        )
        OrderCostLineItem(
            label = stringResource(R.string.checkout_review_estimated_tax),
            value = "$-",
        )
        OrderCostLineItem(
            label = stringResource(R.string.checkout_review_order_total),
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
    user: User,
) {
    Column(modifier = modifier) {
        Text(
            style = MaterialTheme.typography.labelLarge,
            text = stringResource(R.string.checkout_review_paying_with, user.paymentMethodText),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_review_change_payment_method))
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_review_use_alternate_payments))
            },
        )
    }
}

@Composable
private fun DeliveryOverview(
    modifier: Modifier = Modifier,
    user: User,
) {
    Column(modifier = modifier) {
        val verticalSpacing = 16.dp

        Text(
            style = MaterialTheme.typography.labelLarge,
            text = stringResource(R.string.checkout_review_deliver_to, user.deliveryFullName),
        )

        Text(
            text = user.deliveryAddress,
        )

        Spacer(modifier = Modifier.height(verticalSpacing))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_review_change_delivery_address))
            },
        )

        Spacer(modifier = Modifier.height(verticalSpacing))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_review_add_delivery_instructions))
            },
        )

        Spacer(modifier = Modifier.height(verticalSpacing))

        Text(
            text = buildAnnotatedString {
                appendLink(stringResource(R.string.checkout_review_free_pickup_near_address))
            },
        )
    }
}

@Composable
fun PlaceOrderCtaAndFinalTos(modifier: Modifier) {
    Column(modifier = modifier) {
        PrimaryCta(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = stringResource(R.string.checkout_review_place_order_cta),
        )


        val disclaimerHtml = AnnotatedString.fromHtml(
            htmlString = stringResource(R.string.checkout_review_final_disclaimer_html),
            linkStyles = TextLinkStyles(style = SpanStyle(color = LinkBlue)),
        )
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = disclaimerHtml,
        )
    }
}

@Preview
@Composable
private fun CheckoutReviewPreview() {
    Surface {
        CheckoutReviewScreenRoot(modifier = Modifier)
    }
}
