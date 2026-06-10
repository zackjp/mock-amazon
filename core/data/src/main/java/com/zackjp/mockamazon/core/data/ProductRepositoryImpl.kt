package com.zackjp.mockamazon.core.data

import com.zackjp.mockamazon.core.data.remote.ProductFakeApiDataSource
import com.zackjp.mockamazon.core.model.ProductInfo
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productFakeApiDataSource: ProductFakeApiDataSource,
) : ProductRepository {

    override suspend fun getProductById(productId: Int): ProductInfo? =
        productFakeApiDataSource.getProductById(productId)

    override suspend fun getSimilarProducts(productId: Int): List<ProductInfo> =
        productFakeApiDataSource.getSimilarProducts(productId)

}
