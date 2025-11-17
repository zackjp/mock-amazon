package com.example.fakeamazon.data

import com.example.fakeamazon.R
import com.example.fakeamazon.shared.model.ProductInfo
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchApiDataSource @Inject constructor(
    private val productInMemoryDb: ProductInMemoryDb,
) {

    suspend fun getHardcodedSearchResults(searchString: String): List<ProductInfo> {
        delay(2000)
        return productInMemoryDb.getSimilarProducts(R.drawable.item_snack_amazon_pbpretzels)
    }

}
