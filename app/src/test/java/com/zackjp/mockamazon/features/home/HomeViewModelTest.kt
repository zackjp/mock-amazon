package com.zackjp.mockamazon.features.home

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.zackjp.mockamazon.R
import com.zackjp.mockamazon.data.HomeRepository
import com.zackjp.mockamazon.shared.model.Item
import com.zackjp.mockamazon.shared.model.ItemGroup
import com.zackjp.mockamazon.shared.model.ItemSection
import com.zackjp.mockamazon.shared.model.TopHomeGroup
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
                    Item(id = 123, imageRes = 123, discount = 0.01f),
                    Item(id = 234, imageRes = 234, discount = 0.02f),
                    Item(id = 345, imageRes = 345, discount = 0.03f),
                    Item(id = 456, imageRes = 456, discount = 0.04f),
                ),
            )
        ),
        ItemSection(
            "Section Title 2",
            listOf(
                ItemGroup(
                    "Group heading 2a",
                    Item(id = 321, imageRes = 321, discount = 0.11f),
                    Item(id = 432, imageRes = 432, discount = 0.12f),
                    Item(id = 543, imageRes = 543, discount = 0.13f),
                    Item(id = 654, imageRes = 654, discount = 0.14f),
                ),
            ),
        ),
    )

    private val mockTopHomeGroups: List<TopHomeGroup> = listOf(
        TopHomeGroup(
            "TopHome Title 1",
            Color.Black,
            listOf(
                Item(id = R.drawable.item_headphones, imageRes = R.drawable.item_headphones),
                Item(id = R.drawable.item_backpack, imageRes = R.drawable.item_backpack),
            )
        ),
        TopHomeGroup(
            "TopHome Title 2",
            Color.White,
            listOf(
                Item(id = R.drawable.item_kitchen_sponge, imageRes = R.drawable.item_kitchen_sponge),
                Item(id = R.drawable.item_matcha, imageRes = R.drawable.item_matcha),
            )
        ),
    )

    lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        coEvery { mockHomeRepository.getHomeSections() } returns mockSections
        coEvery { mockHomeRepository.getTopHomeGroups() } returns mockTopHomeGroups

        viewModel = HomeViewModel(mockHomeRepository)
    }

    @Test
    fun viewModel_Init_StartsAsLoading() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe HomeScreenState.Loading
        }
    }

    @Test
    fun viewModel_Load_LoadsTopHomeAndHomeSectionsAsync() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe HomeScreenState.Loading

            viewModel.load()

            awaitItem().shouldBeInstanceOf<HomeScreenState.Loaded> {
                it.topHomeGroups shouldBe mockTopHomeGroups
                it.homeSections shouldNot beEmpty()
            }
        }

    }

    @Test
    fun viewModel_Load_EmitsErrorStateIfErred() = runTest {
        coEvery { mockHomeRepository.getHomeSections() } throws Exception("cancellation test")

        viewModel.screenState.test {
            viewModel.load()
            awaitItem() shouldBe HomeScreenState.Loading
            awaitItem() shouldBe HomeScreenState.Error
        }
    }

}
