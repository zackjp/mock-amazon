package com.example.mockamazon.data

import com.example.mockamazon.shared.model.CartItem
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
