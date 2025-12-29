package com.zackjp.mockamazon.checkout.ui.model

import com.zackjp.mockamazon.shared.model.Cart
import com.zackjp.mockamazon.shared.model.User


sealed class CheckoutState {
    data object Error : CheckoutState()
    data class Loaded(
        val cart: Cart,
        val user: User,
    ) : CheckoutState()
    data object Loading : CheckoutState()
}

sealed class CheckoutEffect
