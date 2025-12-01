package com.example.mockamazon.features.cart

import com.example.mockamazon.shared.model.CartItem

sealed class CartScreenState {
    object Loading : CartScreenState()
    data class Loaded(
        val cartItems: List<CartItem>,
        val isReloading: Boolean = false,
    ) : CartScreenState()
    object Error : CartScreenState()

}
