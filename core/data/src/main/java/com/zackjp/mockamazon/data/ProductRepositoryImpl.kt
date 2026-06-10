package com.zackjp.mockamazon.data

import com.zackjp.mockamazon.data.remote.ProductFakeApiDataSource
import com.zackjp.mockamazon.model.ProductInfo
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productFakeApiDataSource: ProductFakeApiDataSource,
) : ProductRepository {

    override suspend fun getProductById(productId: Int): ProductInfo? =
        productFakeApiDataSource.getProductById(productId)

    override suspend fun getSimilarProducts(productId: Int): List<ProductInfo> =
        productFakeApiDataSource.getSimilarProducts(productId)

}
