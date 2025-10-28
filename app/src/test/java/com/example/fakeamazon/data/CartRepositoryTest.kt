package com.example.fakeamazon.data

import com.example.fakeamazon.R
import com.example.fakeamazon.shared.model.ProductInfo
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CartRepositoryTest {

    @Test
    fun getCartItem_ReturnsItem() = runTest {
        val productDataSource = mockk<ProductStaticDataSource>()
        val productInfo = ProductInfo(
            123,
            "Store Name1",
            "SN1",
            "Title 1",
            2.3f,
            R.drawable.item_game_lost_cities
        )
        every { productDataSource.getProductById(R.drawable.item_game_lost_cities) } returns productInfo
        val repo = CartRepository(productDataSource)

        repo.getCartItem() shouldNotBeNull {
            id shouldBe productInfo.id
            imageId shouldBe productInfo.imageId
            title shouldBe productInfo.title
        }
    }

}
