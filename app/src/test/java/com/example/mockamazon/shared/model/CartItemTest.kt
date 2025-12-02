package com.example.mockamazon.shared.model

import java.time.LocalDate

fun Cart.Companion.fakeCart(cartItems: List<CartItem>) = Cart(
    cartItems = cartItems,
    totalPriceUSD = cartItems.sumOf { it.priceUSD.toDouble() }.toFloat(),
)

fun CartItem.Companion.fakeItem(
    number: Int,
): CartItem =
    CartItem(
        id = number,
        imageId = number,
        title = "Title $number",
        quantity = number,
        priceUSD = number * 10f,
        deliveryCostUSD = number * .1f,
        estDeliveryDate = LocalDate.of(2025, 11, 11),
        isInStock = true,
    )
