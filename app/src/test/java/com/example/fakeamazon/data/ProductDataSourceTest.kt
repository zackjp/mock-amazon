package com.example.fakeamazon.data

import com.example.fakeamazon.R
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ProductDataSourceTest {

    @Test
    fun getProductById_WithValidProductId_ReturnsInfo() {
        val dataSource = ProductStaticDataSource()

        dataSource.getProductById(R.drawable.item_backpack) shouldNotBeNull {}
    }

    @Test
    fun getProductById_WithInvalidProductId_ReturnsNull() {
        val dataSource = ProductStaticDataSource()

        dataSource.getProductById(0) shouldBe null
    }

}