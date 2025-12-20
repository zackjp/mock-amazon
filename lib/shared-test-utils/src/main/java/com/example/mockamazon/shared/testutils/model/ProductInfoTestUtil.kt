package com.example.mockamazon.shared.testutils.model

import com.example.mockamazon.shared.model.ProductCategory
import com.example.mockamazon.shared.model.ProductInfo


fun ProductInfo.Companion.fakeInfo(
    number: Int,
    category: ProductCategory = ProductCategory.UNKNOWN,
    title: String? = null,
): ProductInfo =
    ProductInfo(
        id = number,
        storeName = "Store Name $number",
        storeInitials = "SI$number",
        title = title ?: "Title $number",
        productRating = number % 5 + 0.5f,
        imageId = number,
        priceUSD = number * 10f,
        category = category,
        discount = number * .01f
    )
