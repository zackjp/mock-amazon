package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.model.toCartItem
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartFakeApiDataSource @Inject constructor(
    private val productInMemoryDb: ProductInMemoryDb,
) {

    val cartProductIds = mutableListOf<Int>()

    suspend fun addToCart(productId: Int): Boolean {
        delay(500)

        val productInfo = productInMemoryDb.getProductById(productId)
        return productInfo != null && cartProductIds.add(productId)
    }

    suspend fun getCartItems(): List<CartItem> {
        delay(300)

        return cartProductIds
            .map { productInMemoryDb.getProductById(it) }
            .filter { it != null }
            .map { it!!.toCartItem() }
    }

}
