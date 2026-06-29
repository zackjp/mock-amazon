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
        withContext(dispatcherProvider.io) {
            delay(1000) // simulate network delay
            createHeroCarouselCards()
        }

    suspend fun fetchIntentCarousels(): List<IntentCarouselResponse> =
        withContext(dispatcherProvider.io) {
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
            heroId = "hero_official_soccer_merch",
            preTitle = "Official soccer merch",
            title = "Get ready for the knockout rounds",
            background = Color(0xFF337a2b),
            backgroundImageId = R.drawable.hero_bg_official_soccer_merch__get_ready_for_the_knockout_rounds_lg,
            productGridHeightFraction = 0f,
            productTileResponse = emptyList()
        ),
        HeroCarouselCardResponse(
            heroId = "hero_top_100_apartment_finds",
            title = "Top 100+ apartment finds",
            background = Color(0xFF972AF7),
            backgroundImageId = R.drawable.hero_bg_top_100_finds_lg,
            productGridHeightFraction = 0.54f,
            productTileResponse = listOf(
                getProductAsItem(R.drawable.item_snack_amazon_pbpretzels),
                getProductAsItem(R.drawable.item_water_filter),
                getProductAsItem(R.drawable.item_deodorant),
                getProductAsItem(R.drawable.item_soda),
            )
        ),
        HeroCarouselCardResponse(
            heroId = "hero_more_top_picks",
            title = "More top picks for you",
            background = Color(0xFF58ADE3),
            productGridHeightFraction = 0.8f,
            productTileResponse = listOf(
                getProductAsItem(R.drawable.item_sneaker_allbirds_treeglider),
                getProductAsItem(R.drawable.item_sneaker_adidas_ubounce),
                getProductAsItem(R.drawable.item_sneaker_allbirds_treedasher2),
                getProductAsItem(R.drawable.item_sneaker_underarmour_chargedassert9),
                getProductAsItem(R.drawable.item_sneaker_adidas_swiftrun1),
            )
        ),
        HeroCarouselCardResponse(
            heroId = "hero_4plus_stars_under_25",
            preTitle = "Spend less every day",
            title = "Shop 4+ star finds under $25",
            background = Color(0xFFF5DF03),
            backgroundImageId = R.drawable.hero_bg_4plus_star_finds_under_25_lg,
            productGridHeightFraction = 0.54f,
            productTileResponse = listOf(
                getProductAsItem(R.drawable.item_household_yankee_candle_ocean_air_sm),
                getProductAsItem(R.drawable.item_hats_adidas_bucket_hat_sm),
                getProductAsItem(R.drawable.item_household_tervis_travel_cups_sm),
                getProductAsItem(R.drawable.item_clothing_wrangler_jean_shorts_sm),
            )
        ),
        HeroCarouselCardResponse(
            heroId = "hero_amazon_picks",
            title = "Amazon picks for you",
            background = Color(0xFFED7571),
            productGridHeightFraction = 0.8f,
            productTileResponse = listOf(
                getProductAsItem(R.drawable.item_game_monopoly_deal),
                getProductAsItem(R.drawable.item_game_catan),
                getProductAsItem(R.drawable.item_game_ra),
                getProductAsItem(R.drawable.item_game_lost_cities),
                getProductAsItem(R.drawable.item_game_forest_shuffle),
            )
        ),
        HeroCarouselCardResponse(
            heroId = "hero_snack_time",
            title = "Snack time for everyone",
            background = Color(0xFF6AD17D),
            productGridHeightFraction = 0.8f,
            productTileResponse = listOf(
                getProductAsItem(R.drawable.item_snack_larabar_pbchocolatechip),
                getProductAsItem(R.drawable.item_snack_wonderful_pistachios),
                getProductAsItem(R.drawable.item_snack_bluediamond_almonds),
                getProductAsItem(R.drawable.item_snack_dots_originalpretzels),
                getProductAsItem(R.drawable.item_snack_popsecret_popcorn),
            )
        ),
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