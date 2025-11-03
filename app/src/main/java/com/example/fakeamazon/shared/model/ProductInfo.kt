package com.example.fakeamazon.shared.model

import androidx.annotation.DrawableRes
import java.time.LocalDate


data class ProductInfo(
    val id: Int,
    val storeName: String,
    val storeInitials: String,
    val title: String,
    val productRating: Float,
    @param:DrawableRes val imageId: Int,
    val priceUSD: Float,
    val deliveryDate: LocalDate = LocalDate.now().plusDays(2),
    val discount: Float? = null,
) {
    companion object
}
