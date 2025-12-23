package com.zackjp.mockamazon.features.product.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.zackjp.mockamazon.R
import com.zackjp.mockamazon.shared.theme.Green60
import com.zackjp.mockamazon.shared.theme.LinkBlue
import com.zackjp.mockamazon.shared.theme.Teal60
import com.zackjp.mockamazon.shared.toPrimeDeliveryString
import com.zackjp.mockamazon.shared.toRelativeDateString
import com.zackjp.mockamazon.shared.ui.PriceDisplaySize
import com.zackjp.mockamazon.shared.ui.PriceText
import com.zackjp.mockamazon.shared.ui.PrimaryCta
import com.zackjp.mockamazon.shared.ui.getPrimeLogoTextInfo
import com.zackjp.mockamazon.features.product.AddToCartState
import java.time.LocalDate
import kotlin.text.Typography.nbsp


@Composable
fun PurchaseInfoView(
    addToCartState: AddToCartState,
    deliveryDate: LocalDate,
    modifier: Modifier = Modifier,
    onAddToCart: () -> Unit,
    priceUSD: Float,
) {
    Column(modifier = modifier) {
        PriceText(
            displaySize = PriceDisplaySize.SP38,
            modifier = Modifier,
            priceUSD = priceUSD,
        )

        Spacer(modifier.height(16.dp))

        val context = LocalContext.current

        val primeLogoTextInfo = getPrimeLogoTextInfo()
        Text(
            fontWeight = FontWeight.Bold,
            inlineContent = primeLogoTextInfo.inlineContent,
            text = buildAnnotatedString {
                append(primeLogoTextInfo.primeLogoText)

                val primeDeliveryString =
                    deliveryDate.toPrimeDeliveryString(context)
                primeDeliveryString?.let {
                    append(" $primeDeliveryString")
                }
            },
        )

        Spacer(modifier.height(4.dp))

        Text(
            text = buildAnnotatedString {
                append("FREE delivery ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(deliveryDate.toRelativeDateString(context, true))
                }
                append(" Order within ")
                withStyle(SpanStyle(color = Teal60)) {
                    append("4${nbsp}hrs${nbsp}29${nbsp}mins") // todo
                }
            }
        )

        Spacer(modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = LinkBlue)) {
                    append("Deliver to John Doe - New York 10101")
                }
            }
        )

        Spacer(modifier.height(12.dp))

        val bodyLarge = MaterialTheme.typography.bodyLarge
        Text(
            color = Green60,
            style = bodyLarge.copy(fontSize = bodyLarge.fontSize * 1.12),
            text = "In Stock",
        )

        Spacer(modifier.height(16.dp))

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
