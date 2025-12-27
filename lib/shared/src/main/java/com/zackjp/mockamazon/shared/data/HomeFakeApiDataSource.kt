package com.zackjp.mockamazon.shared.data

import androidx.compose.ui.graphics.Color
import com.zackjp.mockamazon.shared.DispatcherProvider
import com.zackjp.mockamazon.shared.model.Item
import com.zackjp.mockamazon.shared.model.ItemGroup
import com.zackjp.mockamazon.shared.model.ItemSection
import com.zackjp.mockamazon.shared.model.ProductInfo
import com.zackjp.mockamazon.shared.model.TopHomeGroup
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.zackjp.mockamazon.shared.R as SharedR

@Singleton
class HomeFakeApiDataSource @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val productInMemoryDb: ProductInMemoryDb,
) {

    suspend fun fetchTopHomeGroups(): List<TopHomeGroup> = withContext(dispatcherProvider.default) {
        delay(1000) // simulate network delay
        createTopHomeGroups()
    }

    suspend fun fetchHomeSections(): List<ItemSection> = withContext(dispatcherProvider.default) {
        delay(1000L) // simulate network delay
        createHomeSections()
    }

    private fun getProductAsItem(id: Int, showDiscount: Boolean = true): Item =
        productInMemoryDb.getProductById(id)!!.toItem(showDiscount)

    private fun ProductInfo.toItem(showDiscount: Boolean): Item =
        Item(
            id = id,
            imageRes = imageId,
            discount = if (showDiscount) discount else null,
        )

    private fun createTopHomeGroups(): List<TopHomeGroup> = listOf(
        TopHomeGroup(
            "More top\npicks for you",
            Color(0xFF3A6DB1),
            listOf(
                getProductAsItem(SharedR.drawable.item_sneaker_allbirds_treeglider),
                getProductAsItem(SharedR.drawable.item_sneaker_adidas_ubounce),
                getProductAsItem(SharedR.drawable.item_sneaker_allbirds_treedasher2),
                getProductAsItem(SharedR.drawable.item_sneaker_underarmour_chargedassert9),
                getProductAsItem(SharedR.drawable.item_sneaker_adidas_swiftrun1),
            )
        ),
        TopHomeGroup(
            "Snack time\nfor everyone",
            Color(0xFF6AD17D),
            listOf(
                getProductAsItem(SharedR.drawable.item_snack_larabar_pbchocolatechip),
                getProductAsItem(SharedR.drawable.item_snack_wonderful_pistachios),
                getProductAsItem(SharedR.drawable.item_snack_bluediamond_almonds),
                getProductAsItem(SharedR.drawable.item_snack_dots_originalpretzels),
                getProductAsItem(SharedR.drawable.item_snack_popsecret_popcorn),
            )
        ),
        TopHomeGroup(
            "Amazon picks\nfor you",
            Color(0xFFED7571),
            listOf(
                getProductAsItem(SharedR.drawable.item_game_monopoly_deal),
                getProductAsItem(SharedR.drawable.item_game_catan),
                getProductAsItem(SharedR.drawable.item_game_ra),
                getProductAsItem(SharedR.drawable.item_game_lost_cities),
                getProductAsItem(SharedR.drawable.item_game_forest_shuffle),
            )
        )
    )

    private fun createHomeSections(): List<ItemSection> = listOf(
        ItemSection(
            "Recommended deals for you",
            listOf(
                ItemGroup(
                    "Deals for you",
                    getProductAsItem(SharedR.drawable.item_backpack),
                    getProductAsItem(SharedR.drawable.item_headphones),
                    getProductAsItem(SharedR.drawable.item_detergent),
                    getProductAsItem(SharedR.drawable.item_dishwash_detergent),
                ),
                ItemGroup(
                    "Inspired by your recent history",
                    getProductAsItem(SharedR.drawable.item_handsoap),
                    getProductAsItem(SharedR.drawable.item_sandwich_bags),
                    getProductAsItem(SharedR.drawable.item_matcha),
                    getProductAsItem(SharedR.drawable.item_kitchen_sponge),
                )
            )
        ),
        ItemSection(
            "Buy again",
            listOf(
                ItemGroup(
                    "Reorder soon",
                    getProductAsItem(SharedR.drawable.item_deodorant),
                    getProductAsItem(SharedR.drawable.item_soda),
                    getProductAsItem(SharedR.drawable.item_water_filter),
                    getProductAsItem(SharedR.drawable.item_cleaning_gloves),
                ),
                ItemGroup(
                    "Home & Kitchen",
                    getProductAsItem(SharedR.drawable.item_kitchen_sponge),
                    getProductAsItem(SharedR.drawable.item_dishwash_detergent),
                    getProductAsItem(SharedR.drawable.item_handsoap),
                    getProductAsItem(SharedR.drawable.item_sandwich_bags),
                )
            )
        ),
    )

}