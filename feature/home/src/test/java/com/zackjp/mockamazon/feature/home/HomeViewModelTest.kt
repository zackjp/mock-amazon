package com.zackjp.mockamazon.feature.home

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.zackjp.mockamazon.core.data.HomeRepository
import com.zackjp.mockamazon.core.model.CarouselCard
import com.zackjp.mockamazon.core.model.CarouselItem
import com.zackjp.mockamazon.core.model.CategoryCarousel
import com.zackjp.mockamazon.core.model.HeroCarouselCard
import com.zackjp.mockamazon.shared.R
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
    val mockCategoryCarousels = listOf(
        CategoryCarousel(
            "Section Title 1",
            listOf(
                CarouselCard(
                    "Group heading 1",
                    CarouselItem(id = 123, imageRes = 123, discount = 0.01f),
                    CarouselItem(id = 234, imageRes = 234, discount = 0.02f),
                    CarouselItem(id = 345, imageRes = 345, discount = 0.03f),
                    CarouselItem(id = 456, imageRes = 456, discount = 0.04f),
                ),
            )
        ),
        CategoryCarousel(
            "Section Title 2",
            listOf(
                CarouselCard(
                    "Group heading 2a",
                    CarouselItem(id = 321, imageRes = 321, discount = 0.11f),
                    CarouselItem(id = 432, imageRes = 432, discount = 0.12f),
                    CarouselItem(id = 543, imageRes = 543, discount = 0.13f),
                    CarouselItem(id = 654, imageRes = 654, discount = 0.14f),
                ),
            ),
        ),
    )

    private val mockHeroCarousel: List<HeroCarouselCard> = listOf(
        HeroCarouselCard(
            "TopHome Title 1",
            Color.Black,
            listOf(
                CarouselItem(id = R.drawable.item_headphones, imageRes = R.drawable.item_headphones),
                CarouselItem(id = R.drawable.item_backpack, imageRes = R.drawable.item_backpack),
            )
        ),
        HeroCarouselCard(
            "TopHome Title 2",
            Color.White,
            listOf(
                CarouselItem(
                    id = R.drawable.item_kitchen_sponge,
                    imageRes = R.drawable.item_kitchen_sponge
                ),
                CarouselItem(id = R.drawable.item_matcha, imageRes = R.drawable.item_matcha),
            )
        ),
    )

    lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        coEvery { mockHomeRepository.getCategoryCarousels() } returns mockCategoryCarousels
        coEvery { mockHomeRepository.getHeroCarouselCards() } returns mockHeroCarousel

        viewModel = HomeViewModel(mockHomeRepository)
    }

    @Test
    fun viewModel_Init_StartsAsLoading() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe HomeScreenState.Loading
        }
    }

    @Test
    fun viewModel_Load_LoadsHeroCarouselAndCategoryCarouselsAsync() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe HomeScreenState.Loading

            awaitItem().shouldBeInstanceOf<HomeScreenState.Loaded> {
                it.heroCarouselCards shouldBe mockHeroCarousel
                it.categoryCarousels shouldNot beEmpty()
            }
        }
    }

    @Test
    fun viewModel_Load_EmitsErrorStateIfErred() = runTest {
        coEvery { mockHomeRepository.getCategoryCarousels() } throws Exception("cancellation test")

        viewModel.screenState.test {
            awaitItem() shouldBe HomeScreenState.Loading
            awaitItem() shouldBe HomeScreenState.Error
        }
    }

}