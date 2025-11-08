package com.example.fakeamazon.data

import androidx.compose.ui.graphics.Color
import com.example.fakeamazon.R
import com.example.fakeamazon.shared.DispatcherProvider
import com.example.fakeamazon.shared.model.Item
import com.example.fakeamazon.shared.model.ItemGroup
import com.example.fakeamazon.shared.model.ItemSection
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.TopHomeGroup
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

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
                getProductAsItem(R.drawable.item_sneaker_allbirds_treeglider),
                getProductAsItem(R.drawable.item_sneaker_adidas_ubounce),
                getProductAsItem(R.drawable.item_sneaker_allbirds_treedasher2),
                getProductAsItem(R.drawable.item_sneaker_underarmour_chargedassert9),
                getProductAsItem(R.drawable.item_sneaker_adidas_swiftrun1),
            )
        ),
        TopHomeGroup(
            "Snack time\nfor everyone",
            Color(0xFF6AD17D),
            listOf(
                getProductAsItem(R.drawable.item_snack_larabar_pbchocolatechip),
                getProductAsItem(R.drawable.item_snack_wonderful_pistachios),
                getProductAsItem(R.drawable.item_snack_bluediamond_almonds),
                getProductAsItem(R.drawable.item_snack_dots_originalpretzels),
                getProductAsItem(R.drawable.item_snack_popsecret_popcorn),
            )
        ),
        TopHomeGroup(
            "Amazon picks\nfor you",
            Color(0xFFED7571),
            listOf(
                getProductAsItem(R.drawable.item_game_monopoly_deal),
                getProductAsItem(R.drawable.item_game_catan),
                getProductAsItem(R.drawable.item_game_ra),
                getProductAsItem(R.drawable.item_game_lost_cities),
                getProductAsItem(R.drawable.item_game_forest_shuffle),
            )
        )
    )

    private fun createHomeSections(): List<ItemSection> = listOf(
        ItemSection(
            "Recommended deals for you",
            listOf(
                ItemGroup(
                    "Deals for you",
                    getProductAsItem(R.drawable.item_backpack),
                    getProductAsItem(R.drawable.item_headphones),
                    getProductAsItem(R.drawable.item_detergent),
                    getProductAsItem(R.drawable.item_dishwash_detergent),
                ),
                ItemGroup(
                    "Inspired by your recent history",
                    getProductAsItem(R.drawable.item_handsoap),
                    getProductAsItem(R.drawable.item_sandwich_bags),
                    getProductAsItem(R.drawable.item_matcha),
                    getProductAsItem(R.drawable.item_kitchen_sponge),
                )
            )
        ),
        ItemSection(
            "Buy again",
            listOf(
                ItemGroup(
                    "Reorder soon",
                    getProductAsItem(R.drawable.item_deodorant),
                    getProductAsItem(R.drawable.item_soda),
                    getProductAsItem(R.drawable.item_water_filter),
                    getProductAsItem(R.drawable.item_cleaning_gloves),
                ),
                ItemGroup(
                    "Home & Kitchen",
                    getProductAsItem(R.drawable.item_kitchen_sponge),
                    getProductAsItem(R.drawable.item_dishwash_detergent),
                    getProductAsItem(R.drawable.item_handsoap),
                    getProductAsItem(R.drawable.item_sandwich_bags),
                )
            )
        ),
    )

}