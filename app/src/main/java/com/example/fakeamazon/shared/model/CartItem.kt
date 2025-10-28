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
