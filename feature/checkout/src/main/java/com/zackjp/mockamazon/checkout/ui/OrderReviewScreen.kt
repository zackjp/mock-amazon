package com.zackjp.mockamazon.checkout.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zackjp.mockamazon.checkout.R
import com.zackjp.mockamazon.shared.ui.PrimaryCta


@Composable
fun OrderReviewScreenRoot(
    modifier: Modifier = Modifier,
) {
    val mainContentPadding = dimensionResource(R.dimen.main_content_padding_horizontal)
    Surface(
        modifier = modifier
            .padding(horizontal = mainContentPadding, vertical = 16.dp),
    ) {
        LazyColumn {
            item {
                TosAndPlaceOrderCta(modifier = Modifier.fillMaxWidth())
            }

            item {
                OrderTotalSummary(modifier = Modifier.fillMaxWidth())
            }
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
private fun OrderTotalSummary(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val priceBreakdownMap = mapOf(
            R.string.checkout_subtotal_label to "$-",
            R.string.checkout_shipping_and_handling_label to "$-",
            R.string.checkout_estimated_tax_label to "$-",
            R.string.checkout_order_total_label to "$-",
        )

        priceBreakdownMap.forEach { (stringResId, costUSD) ->
            Row(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(stringResId),
                )
                Text(
                    modifier = Modifier,
                    text = costUSD,
                )
            }
        }
    }
}

@Preview
@Composable
private fun OrderReviewPreview() {
    Surface {
        OrderReviewScreenRoot(modifier = Modifier)
    }
}