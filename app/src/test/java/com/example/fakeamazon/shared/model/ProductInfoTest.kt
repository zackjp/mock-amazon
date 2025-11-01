package com.example.fakeamazon.shared.model


fun ProductInfo.Companion.fakeInfo(number: Int): ProductInfo =
    ProductInfo(
        id = number,
        storeName = "Store Name $number",
        storeInitials = "SI$number",
        title = "Title $number",
        productRating = number % 5 + 0.5f,
        imageId = number,
        discount = number * .01f
    )
