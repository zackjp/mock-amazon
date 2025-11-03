package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.ProductInfo
import javax.inject.Inject

class ProductRepository @Inject() constructor(
    private val productFakeApiDataSource: ProductFakeApiDataSource,
) {

    suspend fun getProductById(productId: Int): ProductInfo? =
        productFakeApiDataSource.getProductById(productId)

}
