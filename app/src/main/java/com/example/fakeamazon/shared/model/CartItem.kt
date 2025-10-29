package com.example.fakeamazon.shared.model

import androidx.annotation.DrawableRes
import java.time.LocalDate


data class CartItem(
    val id: Int,
    @param:DrawableRes val imageId: Int,
    val title: String,
    val priceUSD: Float, // we'll want this in local currency, but for now we indicate it's USD only
    val deliveryCostUSD: Float,
    val estDeliveryDate: LocalDate,
    val isInStock: Boolean,
)

fun ProductInfo.toCartItem(): CartItem =
    CartItem(
        id = id,
        imageId = imageId,
        title = title,
        priceUSD = 19.99f,
        deliveryCostUSD = 0f,
        estDeliveryDate = LocalDate.now().plusDays(2),
        isInStock = true,
    )
