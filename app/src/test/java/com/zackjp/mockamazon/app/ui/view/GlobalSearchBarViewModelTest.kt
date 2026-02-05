package com.zackjp.mockamazon.app.ui.view

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GlobalSearchBarViewModelTest {

    @Test
    fun updateSearchText_EmitsNewTextValue() = runTest {
        val viewModel = GlobalSearchBarViewModel()

        val newText = "new search text"
        viewModel.searchText.test {
            viewModel.updateSearchText(TextFieldValue(newText, TextRange(newText.length)))

            awaitItem() shouldBe TextFieldValue("", TextRange.Zero)
            awaitItem() shouldBe TextFieldValue(newText, TextRange(newText.length))
        }
    }

}
