package com.example.mockamazon.features.product.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mockamazon.R
import com.example.mockamazon.shared.ignoreParentPadding
import com.example.mockamazon.shared.model.ProductInfo
import com.example.mockamazon.shared.theme.LinkBlue
import com.example.mockamazon.shared.ui.getRatingStarsTextInfo


@Composable
fun SimilarProductsView(
    horizontalContentPadding: Dp,
    modifier: Modifier = Modifier,
    onViewProduct: (Int) -> Unit,
    similarProducts: List<ProductInfo>,
) {
    Column(modifier = modifier) {
        Text(
            style = with(MaterialTheme.typography.bodyLarge) {
                copy(
                    fontSize = fontSize * 1.15,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = stringResource(R.string.you_might_also_like),
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = horizontalContentPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .ignoreParentPadding(horizontalContentPadding),
        ) {
            items(similarProducts, key = { product -> product.id }) { product ->
                Column(
                    modifier = Modifier
                        .clickable(onClick = { onViewProduct(product.id) })
                        .width(150.dp)
                ) {
                    Image(
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        painter = painterResource(product.imageId),
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        color = LinkBlue,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        text = product.title,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val ratingStarsTextInfo = getRatingStarsTextInfo(product.productRating)
                    Text(
                        fontSize = 20.sp,
                        inlineContent = ratingStarsTextInfo.inlineContent,
                        maxLines = 1,
                        text = ratingStarsTextInfo.text,
                    )
                }
            }
        }
    }
}
