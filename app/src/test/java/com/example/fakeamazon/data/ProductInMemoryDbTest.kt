package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.ProductCategory
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.ints.beGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductInMemoryDbTest {

    private companion object {
        private const val MAX_SIMILAR_PRODUCTS_RESULTS = 7
        private val SNACKS_PRODUCT_ID_RANGE = 100..103
        private val BOARD_GAMES_PRODUCT_ID_RANGE = 200..210
        private val UNKNOWN_CATEGORY_PRODUCT_ID_RANGE = 300..305
        private const val INVALID_PRODUCT_ID = 0
    }

    private val snackProducts: List<ProductInfo> = createFakeProductList(
        productIds = SNACKS_PRODUCT_ID_RANGE,
        category = ProductCategory.SNACKS,
    )
    private val boardGameProducts: List<ProductInfo> = createFakeProductList(
        productIds = BOARD_GAMES_PRODUCT_ID_RANGE,
        category = ProductCategory.BOARD_GAMES,
    )
    private val unknownCategoryProducts: List<ProductInfo> = createFakeProductList(
        productIds = UNKNOWN_CATEGORY_PRODUCT_ID_RANGE,
        category = ProductCategory.UNKNOWN,
    )

    private lateinit var database: ProductInMemoryDb

    @BeforeEach
    fun setUp() {
        database = ProductInMemoryDb(
            products = snackProducts + boardGameProducts + unknownCategoryProducts
        )
    }

    @Test
    fun getProductById_WithValidProductId_ReturnsInfo() {
        val expectedSnackProduct = snackProducts.first()
        val expectedBoardGameProduct = boardGameProducts.last()

        val actualSnackProduct = database.getProductById(expectedSnackProduct.id)
        val actualBoardGameProduct = database.getProductById(expectedBoardGameProduct.id)

        actualSnackProduct shouldBe expectedSnackProduct
        actualBoardGameProduct shouldBe expectedBoardGameProduct
    }

    @Test
    fun getProductById_WithInvalidProductId_ReturnsNull() {
        database.getProductById(INVALID_PRODUCT_ID) should beNull()
    }

    @Test
    fun getSimilarProducts_WithFewResults_ReturnsLessThanMaxResults() {
        withClue("For this test, this list should have less than $MAX_SIMILAR_PRODUCTS_RESULTS products") {
            snackProducts.size shouldBeLessThan MAX_SIMILAR_PRODUCTS_RESULTS
        }
        val firstSnackProductId = snackProducts.first().id

        val similarSnacks = database.getSimilarProducts(firstSnackProductId)

        similarSnacks shouldContainOnly snackProducts.filter { it.id != firstSnackProductId }
    }

    @Test
    fun getSimilarProducts_WithTooManyResults_ReturnsOnlyMaxResults() {
        withClue("For this test, this list should have more than $MAX_SIMILAR_PRODUCTS_RESULTS products") {
            boardGameProducts.size shouldBeGreaterThan MAX_SIMILAR_PRODUCTS_RESULTS
        }
        val lastBoardGameProductId = boardGameProducts.last().id

        val similarGames = database.getSimilarProducts(lastBoardGameProductId)

        similarGames.size shouldBe MAX_SIMILAR_PRODUCTS_RESULTS
        boardGameProducts.filter { it.id != lastBoardGameProductId } shouldContainAll similarGames
    }

    @Test
    fun getSimilarProducts_WithInvalidProductId_ReturnsEmptyList() {
        val actual = database.getSimilarProducts(INVALID_PRODUCT_ID)

        actual shouldBe emptyList()
    }

    @Test
    fun getSimilarProducts_WithUnknownCategory_ReturnsEmptyList() {
        unknownCategoryProducts.size should beGreaterThan(1)

        val firstUnknownProductId = unknownCategoryProducts.first().id

        val productInfo = database.getProductById(firstUnknownProductId)
        withClue("Product with a ${ProductCategory.UNKNOWN.name} category should still be individually retrievable") {
            productInfo shouldBe unknownCategoryProducts.first()
        }

        val similarProducts = database.getSimilarProducts(firstUnknownProductId)
        similarProducts shouldBe emptyList()
    }

    private fun createFakeProductList(
        productIds: IntRange,
        category: ProductCategory
    ): List<ProductInfo> = productIds.map {
        ProductInfo.fakeInfo(
            number = it,
            category = category
        )
    }

}