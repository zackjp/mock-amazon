package com.example.mockamazon.shared.model

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
    val category: ProductCategory,
    val discount: Float? = null,
) {
    companion object
}

enum class ProductCategory() {
    BAGS_AND_CASES,
    BEVERAGES_TEA,
    BEVERAGES_SOFT_DRINKS,
    BOARD_GAMES,
    HEADPHONES,
    HOUSEHOLD_SUPPLIES,
    SNACKS,
    SNEAKERS,
    TOILETRIES,
    UNKNOWN,
}
