package com.zackjp.mockamazon.feature.cart.impl.data

import com.zackjp.mockamazon.core.model.Cart
import com.zackjp.mockamazon.feature.cart.api.data.CartRepository
import com.zackjp.mockamazon.feature.cart.impl.datasource.CartFakeApiDataSource
import javax.inject.Inject


internal class CartRepositoryImpl @Inject constructor(
    private val cartFakeApiDataSource: CartFakeApiDataSource,
) : CartRepository {

    override suspend fun getCart(): Cart {
        return cartFakeApiDataSource.getCart()
    }

    override suspend fun setQuantity(productId: Int, quantity: Int) {
        cartFakeApiDataSource.setQuantity(productId, quantity)
    }

    override suspend fun addToCart(productId: Int) {
        cartFakeApiDataSource.addToCart(productId)
    }

    override suspend fun removeByProductId(productId: Int) {
        cartFakeApiDataSource.removeByProductId(productId)
    }

    override suspend fun decrementByProductId(productId: Int) {
        cartFakeApiDataSource.decrementByProductId(productId)
    }

}
