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
            intentId = "intent_id_1",
            title = "Section Title 1",
            contextCards = listOf(
                ContextCard(
                    contextId = "context_id_1",
                    title = "Context title 1",
                    rec1 = ProductTile(productId = 123, imageRes = 123, discount = 0.01f),
                    rec2 = ProductTile(productId = 234, imageRes = 234, discount = 0.02f),
                    rec3 = ProductTile(productId = 345, imageRes = 345, discount = 0.03f),
                    rec4 = ProductTile(productId = 456, imageRes = 456, discount = 0.04f),
                ),
            )
        ),
        IntentCarousel(
            intentId = "intent_id_2",
            title = "Section Title 2",
            contextCards = listOf(
                ContextCard(
                    contextId = "context_id_2",
                    title = "Context title 2",
                    rec1 = ProductTile(productId = 321, imageRes = 321, discount = 0.11f),
                    rec2 = ProductTile(productId = 432, imageRes = 432, discount = 0.12f),
                    rec3 = ProductTile(productId = 543, imageRes = 543, discount = 0.13f),
                    rec4 = ProductTile(productId = 654, imageRes = 654, discount = 0.14f),
                ),
            ),
        ),
    )

    private val mockHeroCarousel: List<HeroCarouselCard> = listOf(
        HeroCarouselCard(
            heroId = "hero_id_1",
            title = "Hero Title 1",
            background = Color.Black,
            backgroundImageId = 123,
            productGridHeightFraction = 0.12f,
            productTiles = listOf(
                ProductTile(productId = R.drawable.item_headphones, imageRes = R.drawable.item_headphones),
                ProductTile(productId = R.drawable.item_backpack, imageRes = R.drawable.item_backpack),
            )
        ),
        HeroCarouselCard(
            heroId = "hero_id_2",
            title = "Hero Title 2",
            background = Color.White,
            backgroundImageId = 456,
            productGridHeightFraction = 0.45f,
            productTiles = listOf(
                ProductTile(
                    productId = R.drawable.item_kitchen_sponge,
                    imageRes = R.drawable.item_kitchen_sponge
                ),
                ProductTile(productId = R.drawable.item_matcha, imageRes = R.drawable.item_matcha),
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