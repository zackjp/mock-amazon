package com.example.fakeamazon.features.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fakeamazon.R
import com.example.fakeamazon.app.AMAZON_BEIGE
import com.example.fakeamazon.model.ProductInfo

@Composable
fun ProductScreenRoot(
    modifier: Modifier = Modifier,
    productId: Int,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    LaunchedEffect(productId) {
        viewModel.load(productId)
    }

    val productInfo by viewModel.productInfo.collectAsStateWithLifecycle()

    productInfo?.let { productInfo ->
        ProductScreen(
            modifier = modifier,
            productInfo = productInfo,
        )
    }
}

@Composable
private fun ProductScreen(
    modifier: Modifier = Modifier,
    productInfo: ProductInfo,
) {
    val mainContentPadding = dimensionResource(R.dimen.main_content_padding_horizontal)

    Surface(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = mainContentPadding, end = mainContentPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                StoreAndProductRatingHeader(
                    modifier = Modifier.fillMaxWidth(),
                    storeName = productInfo.storeName,
                    storeInitials = productInfo.storeInitials,
                    productRating = productInfo.productRating,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = productInfo.title,
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                ProductImage(
                    imageId = productInfo.imageId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
            }
        }
    }
}

@Composable
private fun StoreAndProductRatingHeader(
    modifier: Modifier = Modifier,
    storeName: String,
    storeInitials: String,
    productRating: Float,
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AMAZON_BEIGE)
                .padding(6.dp)
        ) {
            BasicText(
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 14.sp,
                    maxFontSize = 22.sp,
                ),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.displayMedium,
                text = storeInitials,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = storeName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )

            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Visit the Store",
                )

                Text(style = MaterialTheme.typography.bodySmall, text = "$productRating")
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "*****")
            }
        }
    }
}

@Composable
fun ProductImage(modifier: Modifier, imageId: Int) {
    Box(modifier = modifier) {
        Image(
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(imageId),
        )
    }
}
