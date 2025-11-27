package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.CartItem
import kotlinx.coroutines.delay
import javax.inject.Inject


class CartRepository @Inject constructor(
    private val cartFakeApiDataSource: CartFakeApiDataSource,
) {

    suspend fun getCartItems(): List<CartItem> {
        return cartFakeApiDataSource.getCartItems()
    }

    suspend fun setQuantity(productId: Int, quantity: Int) {
        cartFakeApiDataSource.setQuantity(productId, quantity)
    }

    suspend fun addToCart(productId: Int) {
        cartFakeApiDataSource.addToCart(productId)
    }

    suspend fun removeByProductId(productId: Int) {
        cartFakeApiDataSource.removeByProductId(productId)
    }

    suspend fun decrementByProductId(productId: Int) {
        cartFakeApiDataSource.decrementByProductId(productId)
    }

}
