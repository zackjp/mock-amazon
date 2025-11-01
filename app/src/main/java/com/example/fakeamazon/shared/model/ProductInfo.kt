package com.example.fakeamazon.shared.model

import androidx.annotation.DrawableRes


data class ProductInfo(
    val id: Int,
    val storeName: String,
    val storeInitials: String,
    val title: String,
    val productRating: Float,
    @param:DrawableRes val imageId: Int,
    val discount: Float? = null,
) {
    companion object
}
