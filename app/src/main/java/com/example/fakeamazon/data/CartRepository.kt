package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.CartItem
import javax.inject.Inject


class CartRepository @Inject constructor(
    private val cartFakeApiDataSource: CartFakeApiDataSource,
) {

    suspend fun getCartItem(): CartItem? {
        return cartFakeApiDataSource.getCartItems().lastOrNull()
    }

    suspend fun addToCart(productId: Int) {
        cartFakeApiDataSource.addToCart(productId)
    }

}
