package com.zackjp.mockamazon.core.data.remote

import androidx.compose.ui.graphics.Color
import com.zackjp.mockamazon.core.model.ProductInfo
import com.zackjp.mockamazon.shared.DispatcherProvider
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.data.ProductInMemoryDb
import com.zackjp.mockamazon.shared.model.ContextCardResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse
import com.zackjp.mockamazon.shared.model.IntentCarouselResponse
import com.zackjp.mockamazon.shared.model.ProductTileResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomeFakeApiDataSource @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) {

    suspend fun fetchHeroCarouselCards(): List<HeroCarouselCardResponse> =
        withContext(dispatcherProvider.default) {
            delay(1000) // simulate network delay
            createHeroCarouselCards()
        }

    suspend fun fetchIntentCarousels(): List<IntentCarouselResponse> =
        withContext(dispatcherProvider.default) {
            delay(1000L) // simulate network delay
            createIntentCarousels()
        }

    private fun getProductAsItem(id: Int, showDiscount: Boolean = true): ProductTileResponse =
        productInMemoryDb.getProductById(id)!!.toItem(showDiscount)

    private fun ProductInfo.toItem(showDiscount: Boolean): ProductTileResponse =
        ProductTileResponse(
            productId = id,
            imageRes = imageId,
            discount = if (showDiscount) discount else null,
        )

    private fun createHeroCarouselCards(): List<HeroCarouselCardResponse> = listOf(
        HeroCarouselCardResponse(
            heroId = "hero_more_top_picks",
            title = "More top\npicks for you",
            background = Color(0xFF3A6DB1),
            productTileResponse = listOf(
                getProductAsItem(R.drawable.item_sneaker_allbirds_treeglider),
                getProductAsItem(R.drawable.item_sneaker_adidas_ubounce),
                getProductAsItem(R.drawable.item_sneaker_allbirds_treedasher2),
                getProductAsItem(R.drawable.item_sneaker_underarmour_chargedassert9),
                getProductAsItem(R.drawable.item_sneaker_adidas_swiftrun1),
            )
        ),
        HeroCarouselCardResponse(
            heroId = "hero_snack_time",
            title = "Snack time\nfor everyone",
            background = Color(0xFF6AD17D),
            productTileResponse = listOf(
                getProductAsItem(R.drawable.item_snack_larabar_pbchocolatechip),
                getProductAsItem(R.drawable.item_snack_wonderful_pistachios),
                getProductAsItem(R.drawable.item_snack_bluediamond_almonds),
                getProductAsItem(R.drawable.item_snack_dots_originalpretzels),
                getProductAsItem(R.drawable.item_snack_popsecret_popcorn),
            )
        ),
        HeroCarouselCardResponse(
            heroId = "hero_amazon_picks",
            title = "Amazon picks\nfor you",
            background = Color(0xFFED7571),
            productTileResponse = listOf(
                getProductAsItem(R.drawable.item_game_monopoly_deal),
                getProductAsItem(R.drawable.item_game_catan),
                getProductAsItem(R.drawable.item_game_ra),
                getProductAsItem(R.drawable.item_game_lost_cities),
                getProductAsItem(R.drawable.item_game_forest_shuffle),
            )
        )
    )

    private fun createIntentCarousels(): List<IntentCarouselResponse> = listOf(
        IntentCarouselResponse(
            intentId = "intent_recommended_deals",
            title = "Recommended deals for you",
            contextCardResponse = listOf(
                ContextCardResponse(
                    contextId = "context_deals_for_you",
                    title = "Deals for you",
                    rec1 = getProductAsItem(R.drawable.item_backpack),
                    rec2 = getProductAsItem(R.drawable.item_headphones),
                    rec3 = getProductAsItem(R.drawable.item_detergent),
                    rec4 = getProductAsItem(R.drawable.item_dishwash_detergent),
                ),
                ContextCardResponse(
                    contextId = "context_inspired_by_recent_history",
                    title = "Inspired by your recent history",
                    rec1 = getProductAsItem(R.drawable.item_handsoap),
                    rec2 = getProductAsItem(R.drawable.item_sandwich_bags),
                    rec3 = getProductAsItem(R.drawable.item_matcha),
                    rec4 = getProductAsItem(R.drawable.item_kitchen_sponge),
                )
            )
        ),
        IntentCarouselResponse(
            intentId = "intent_buy_again",
            title = "Buy again",
            contextCardResponse = listOf(
                ContextCardResponse(
                    contextId = "context_reorder_soon",
                    title = "Reorder soon",
                    rec1 = getProductAsItem(R.drawable.item_deodorant),
                    rec2 = getProductAsItem(R.drawable.item_soda),
                    rec3 = getProductAsItem(R.drawable.item_water_filter),
                    rec4 = getProductAsItem(R.drawable.item_cleaning_gloves),
                ),
                ContextCardResponse(
                    contextId = "context_home_and_kitchen",
                    title = "Home & Kitchen",
                    rec1 = getProductAsItem(R.drawable.item_kitchen_sponge),
                    rec2 = getProductAsItem(R.drawable.item_dishwash_detergent),
                    rec3 = getProductAsItem(R.drawable.item_handsoap),
                    rec4 = getProductAsItem(R.drawable.item_sandwich_bags),
                )
            )
        ),
    )

}