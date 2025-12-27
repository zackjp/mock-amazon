package com.zackjp.mockamazon.shared.data

import com.zackjp.mockamazon.shared.model.ProductInfo
import javax.inject.Inject

class ProductRepository @Inject() constructor(
    private val productFakeApiDataSource: ProductFakeApiDataSource,
) {

    suspend fun getProductById(productId: Int): ProductInfo? =
        productFakeApiDataSource.getProductById(productId)

    suspend fun getSimilarProducts(productId: Int): List<ProductInfo> =
        productFakeApiDataSource.getSimilarProducts(productId)

}
