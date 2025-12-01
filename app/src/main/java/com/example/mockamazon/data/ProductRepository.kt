package com.example.mockamazon.data

import com.example.mockamazon.shared.model.ProductInfo
import javax.inject.Inject

class ProductRepository @Inject() constructor(
    private val productFakeApiDataSource: ProductFakeApiDataSource,
) {

    suspend fun getProductById(productId: Int): ProductInfo? =
        productFakeApiDataSource.getProductById(productId)

    suspend fun getSimilarProducts(productId: Int): List<ProductInfo> =
        productFakeApiDataSource.getSimilarProducts(productId)

}
