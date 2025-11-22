package com.example.fakeamazon.shared.model

import java.time.LocalDate


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
