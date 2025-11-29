package com.example.fakeamazon.features.cart

import com.example.fakeamazon.shared.model.CartItem

sealed class CartScreenState {
    object Loading : CartScreenState()
    data class Loaded(
        val cartItems: List<CartItem>,
        val isReloading: Boolean = false,
    ) : CartScreenState()
    object Error : CartScreenState()

}
