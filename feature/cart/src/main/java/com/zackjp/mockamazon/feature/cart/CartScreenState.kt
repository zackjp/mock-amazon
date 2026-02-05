package com.zackjp.mockamazon.feature.cart

import com.zackjp.mockamazon.shared.model.CartItem

sealed class CartScreenState {
    data object Loading : CartScreenState()
    data class Loaded(
        val cartItems: List<CartItem>,
        val totalPriceUSD: Float,
        val isReloading: Boolean = false,
    ) : CartScreenState()
    data object Error : CartScreenState()

}
