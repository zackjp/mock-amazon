package com.example.fakeamazon.data

import com.example.fakeamazon.R
import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.model.ProductInfo
import kotlinx.coroutines.delay
import java.time.LocalDate
import javax.inject.Inject


class CartRepository @Inject constructor(
    private val productStaticDataSource: ProductStaticDataSource
) {

    suspend fun getCartItem(): CartItem? {
        delay(500)

        return productStaticDataSource
            .getProductById(R.drawable.item_game_lost_cities)
            ?.toCartItem()
    }

}

private fun ProductInfo.toCartItem(): CartItem =
    CartItem(
        id = id,
        imageId = imageId,
        title = title,
        priceUSD = 19.99f,
        deliveryCostUSD = 0f,
        estDeliveryDate = LocalDate.now().plusDays(2),
        isInStock = true,
    )
