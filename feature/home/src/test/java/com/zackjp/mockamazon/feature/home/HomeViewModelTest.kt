package com.zackjp.mockamazon.feature.home

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.zackjp.mockamazon.core.data.HomeRepository
import com.zackjp.mockamazon.core.model.ContextCard
import com.zackjp.mockamazon.core.model.HeroCarouselCard
import com.zackjp.mockamazon.core.model.IntentCarousel
import com.zackjp.mockamazon.core.model.ProductTile
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.testutils.SetMainCoroutineDispatcher
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
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(SetMainCoroutineDispatcher::class)
class HomeViewModelTest {

    val mockHomeRepository = mockk<HomeRepository>()
    val intentCarousels = listOf(
        IntentCarousel(
            "Section Title 1",
            listOf(
                ContextCard(
                    "Group heading 1",
                    ProductTile(id = 123, imageRes = 123, discount = 0.01f),
                    ProductTile(id = 234, imageRes = 234, discount = 0.02f),
                    ProductTile(id = 345, imageRes = 345, discount = 0.03f),
                    ProductTile(id = 456, imageRes = 456, discount = 0.04f),
                ),
            )
        ),
        IntentCarousel(
            "Section Title 2",
            listOf(
                ContextCard(
                    "Group heading 2a",
                    ProductTile(id = 321, imageRes = 321, discount = 0.11f),
                    ProductTile(id = 432, imageRes = 432, discount = 0.12f),
                    ProductTile(id = 543, imageRes = 543, discount = 0.13f),
                    ProductTile(id = 654, imageRes = 654, discount = 0.14f),
                ),
            ),
        ),
    )

    private val mockHeroCarousel: List<HeroCarouselCard> = listOf(
        HeroCarouselCard(
            "TopHome Title 1",
            Color.Black,
            listOf(
                ProductTile(id = R.drawable.item_headphones, imageRes = R.drawable.item_headphones),
                ProductTile(id = R.drawable.item_backpack, imageRes = R.drawable.item_backpack),
            )
        ),
        HeroCarouselCard(
            "TopHome Title 2",
            Color.White,
            listOf(
                ProductTile(
                    id = R.drawable.item_kitchen_sponge,
                    imageRes = R.drawable.item_kitchen_sponge
                ),
                ProductTile(id = R.drawable.item_matcha, imageRes = R.drawable.item_matcha),
            )
        ),
    )

    lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        coEvery { mockHomeRepository.getIntentCarousels() } returns intentCarousels
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
    fun viewModel_Load_LoadsHeroCarouselAndIntentCarouselsAsync() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe HomeScreenState.Loading

            awaitItem().shouldBeInstanceOf<HomeScreenState.Loaded> {
                it.heroCarouselCards shouldBe mockHeroCarousel
                it.intentCarousels shouldNot beEmpty()
            }
        }
    }

    @Test
    fun viewModel_Load_EmitsErrorStateIfErred() = runTest {
        coEvery { mockHomeRepository.getIntentCarousels() } throws Exception("cancellation test")

        viewModel.screenState.test {
            awaitItem() shouldBe HomeScreenState.Loading
            awaitItem() shouldBe HomeScreenState.Error
        }
    }

}