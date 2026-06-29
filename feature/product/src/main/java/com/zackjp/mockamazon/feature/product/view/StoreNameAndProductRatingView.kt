package com.zackjp.mockamazon.feature.product.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.zackjp.mockamazon.shared.theme.LinkBlue
import com.zackjp.mockamazon.shared.ui.getRatingStarsTextInfo

@Composable
fun StoreNameAndProductRatingView(
    modifier: Modifier = Modifier,
    storeName: String,
    productRating: Float,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                color = LinkBlue,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                text = "Visit the $storeName Store",
            )

            val ratingStarsTextInfo = getRatingStarsTextInfo(productRating)
            val ratingString = buildAnnotatedString {
                append("${ratingStarsTextInfo.normalizedRating} ")
                append(ratingStarsTextInfo.text)
            }

            Text(
                inlineContent = ratingStarsTextInfo.inlineContent,
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.bodySmall,
                text = ratingString,
            )
        }
    }
}
