package com.example.fakeamazon.features.home

import com.example.fakeamazon.data.HomeRepository
import com.example.fakeamazon.model.Item
import com.example.fakeamazon.model.ItemGroup
import com.example.fakeamazon.model.ItemSection
import io.kotest.matchers.ints.shouldBeGreaterThan
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
            "Title 1",
            listOf(
                ItemGroup(
                    "Group heading 1a",
                    Item(123, 0.01f),
                    Item(234, 0.02f),
                    Item(345, 0.03f),
                    Item(456, 0.04f),
                ),
                ItemGroup(
                    "Group heading 1b",
                    Item(567, 0.05f),
                    Item(678, 0.06f),
                    Item(789, 0.07f),
                    Item(890, 0.08f),
                )
            ),
        ),
        ItemSection(
            "Title 2",
            listOf(
                ItemGroup(
                    "Group heading 2a",
                    Item(321, 0.11f),
                    Item(432, 0.12f),
                    Item(543, 0.13f),
                    Item(654, 0.14f),
                ),
                ItemGroup(
                    "Group heading 2b",
                    Item(765, 0.15f),
                    Item(876, 0.16f),
                    Item(987, 0.17f),
                    Item(198, 0.18f),
                )
            ),
        ),
    )


    lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        coEvery { mockHomeRepository.loadSections() } returns mockSections

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
        viewModel.topHomeGroups.first { it.isNotEmpty() }.size shouldBeGreaterThan 0
    }

}
