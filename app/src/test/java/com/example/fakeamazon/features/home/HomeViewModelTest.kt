package com.example.fakeamazon.features.home

import androidx.compose.ui.graphics.Color
import com.example.fakeamazon.R
import com.example.fakeamazon.data.HomeRepository
import com.example.fakeamazon.features.home.model.DisplayableItem
import com.example.fakeamazon.model.Item
import com.example.fakeamazon.model.ItemGroup
import com.example.fakeamazon.model.ItemSection
import com.example.fakeamazon.model.TopHomeGroup
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    val mockHomeRepository = mockk<HomeRepository>()
    val mockSections = listOf(
        ItemSection(
            "Section Title 1",
            listOf(
                ItemGroup(
                    "Group heading 1",
                    Item(123, 0.01f),
                    Item(234, 0.02f),
                    Item(345, 0.03f),
                    Item(456, 0.04f),
                ),
            )
        ),
        ItemSection(
            "Section Title 2",
            listOf(
                ItemGroup(
                    "Group heading 2a",
                    Item(321, 0.11f),
                    Item(432, 0.12f),
                    Item(543, 0.13f),
                    Item(654, 0.14f),
                ),
            ),
        ),
    )

    private val mockTopHomeGroups: List<TopHomeGroup> = listOf(
        TopHomeGroup(
            "TopHome Title 1",
            Color.Black,
            listOf(
                DisplayableItem(imageId = R.drawable.item_headphones),
                DisplayableItem(imageId = R.drawable.item_backpack),
            )
        ),
        TopHomeGroup(
            "TopHome Title 2",
            Color.White,
            listOf(
                DisplayableItem(imageId = R.drawable.item_kitchen_sponge),
                DisplayableItem(imageId = R.drawable.item_matcha),
            )
        ),
    )

    lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        coEvery { mockHomeRepository.loadSections() } returns mockSections
        coEvery { mockHomeRepository.loadTopHome() } returns mockTopHomeGroups

        viewModel = HomeViewModel(mockHomeRepository)
    }

    @Test
    fun viewModel_Init_StartsWithEmptyItemSections() = runTest {
        advanceUntilIdle()

        viewModel.itemSections.value shouldBe emptyList()
    }

    @Test
    fun viewModel_Load_LoadsItemSectionsAsync() = runTest {
        viewModel.load()

        viewModel.itemSections.value shouldBe emptyList()
        advanceUntilIdle()
        viewModel.itemSections.first { it.isNotEmpty() } shouldBe mockSections
    }

    @Test
    fun viewModel_Load_LoadsTopHomeGroupsAsync() = runTest {
        viewModel.load()

        viewModel.topHomeGroups.value shouldBe emptyList()
        advanceUntilIdle()
        viewModel.topHomeGroups.first { it.isNotEmpty() } shouldBe mockTopHomeGroups
    }

}
