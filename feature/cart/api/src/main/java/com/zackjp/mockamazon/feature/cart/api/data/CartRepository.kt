package com.zackjp.mockamazon.feature.cart.api.data

import com.zackjp.mockamazon.core.model.Cart

interface CartRepository {

    suspend fun getCart(): Cart

    suspend fun setQuantity(productId: Int, quantity: Int)

    suspend fun addToCart(productId: Int)

    suspend fun removeByProductId(productId: Int)

    suspend fun decrementByProductId(productId: Int)

}
