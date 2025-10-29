package com.example.fakeamazon.data

import com.example.fakeamazon.R
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import org.junit.jupiter.api.Test

class ProductInMemoryDbTest {

    @Test
    fun getProductById_WithValidProductId_ReturnsInfo() {
        val dataSource = ProductInMemoryDb()

        dataSource.getProductById(R.drawable.item_backpack) shouldNot beNull()
    }

    @Test
    fun getProductById_WithInvalidProductId_ReturnsNull() {
        val dataSource = ProductInMemoryDb()

        dataSource.getProductById(0) should beNull()
    }

}