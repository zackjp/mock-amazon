package com.zackjp.mockamazon.shared.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.toPrimeDeliveryString
import com.zackjp.mockamazon.shared.toRelativeDateString
import com.zackjp.mockamazon.shared.ui.getPrimeLogoTextInfo
import java.time.LocalDate


@Composable
fun PrimeDayText(
    style: TextStyle,
    modifier: Modifier = Modifier,
    estDeliveryDate: LocalDate
) {
    val primeLogoTextInfo = getPrimeLogoTextInfo()
    Text(
        inlineContent = primeLogoTextInfo.inlineContent,
        maxLines = 1,
        modifier = modifier,
        style = style.copy(fontWeight = FontWeight.Bold),
        text = buildAnnotatedString {
            append(primeLogoTextInfo.primeLogoText)
            val primeDeliveryText = estDeliveryDate.toPrimeDeliveryString(LocalContext.current)
            primeDeliveryText?.let { primeDeliveryText ->
                append(" $primeDeliveryText")
            }
        },
    )
}

@Composable
fun ExpectedDeliveryText(
    style: TextStyle,
    estDeliveryDate: LocalDate,
    modifier: Modifier = Modifier,
) {
    Text(
        maxLines = 1,
        modifier = modifier,
        style = style,
        text = buildAnnotatedString {
            val deliveryDate = estDeliveryDate.toRelativeDateString(LocalContext.current, false)
            append(stringResource(R.string.cart_item_free_delivery) + " ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(deliveryDate)
            }
        },
    )
}
