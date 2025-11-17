package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.ProductInfo
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchApiDataSource @Inject constructor(
    private val productInMemoryDb: ProductInMemoryDb,
) {

    suspend fun getSearchResults(searchString: String): List<ProductInfo> {
        delay(2000)
        return productInMemoryDb.findProducts(searchString)
    }

}
