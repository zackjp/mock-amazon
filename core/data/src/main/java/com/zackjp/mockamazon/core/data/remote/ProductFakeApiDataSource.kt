package com.zackjp.mockamazon.core.data.remote

import com.zackjp.mockamazon.core.model.ProductInfo
import com.zackjp.mockamazon.shared.DispatcherProvider
import com.zackjp.mockamazon.shared.data.ProductInMemoryDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProductFakeApiDataSource @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) {

    suspend fun getProductById(productId: Int): ProductInfo? =
        withContext(dispatcherProvider.io) {
            delay(500)
            productInMemoryDb.getProductById(productId)
        }

    suspend fun getSimilarProducts(productId: Int): List<ProductInfo> =
        withContext(dispatcherProvider.io) {
            delay(100)
            productInMemoryDb.getSimilarProducts(productId)
        }

}