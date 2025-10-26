package com.example.fakeamazon.data

import com.example.fakeamazon.R
import com.example.fakeamazon.model.ProductInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDataSource @Inject constructor() {
    private val products = listOf(
        ProductInfo(
            // https://www.amazon.com/Bellroy-Workpack-commuter-backpack-organization/dp/B0DK2ML5X3
            id = R.drawable.item_backpack,
            storeName = "Bellroy",
            storeInitials = "B",
            title = "Bellroy Transit Workpack Pro (22L work bag/commuter backpack, fits 16” laptops; organization for travel and commutes)- Stone",
            productRating = 3.9f,
            imageId = R.drawable.item_backpack,
        ),
        ProductInfo(
            // https://www.amazon.com/Cleanbear-Synthetic-Rubber-Gloves-Medium/dp/B01HJAKV3O
            id = R.drawable.item_cleaning_gloves,
            storeName = "Cleanbear",
            storeInitials = "C",
            title = "Cleanbear Reusable Dish Washing Rubber Glove Set of 3, Household Cleaning Gloves for Washing",
            productRating = 4.6f,
            imageId = R.drawable.item_cleaning_gloves,
        ),
        ProductInfo(
            // https://www.amazon.com/Native-Deodorant-Aluminum-Parabens-Eucalyptus/dp/B07GB41NF9
            id = R.drawable.item_deodorant,
            storeName = "Native",
            storeInitials = "N",
            title = "Native Aluminum-Free Clean Deodorant with Natural Derived Ingredients, 72HR odor control | Deodorant for Women and Men, With Baking Soda, Coconut Oil, Shea Butter | Eucalyptus & Mint",
            productRating = 4.4f,
            imageId = R.drawable.item_deodorant,
        ),
        ProductInfo(
            // https://www.amazon.com/Elements-Natural-Laundry-Detergent-Lavender/dp/B0BSJYJQGQ
            id = R.drawable.item_detergent,
            storeName = "9 Elements",
            storeInitials = "9E",
            title = "9 Elements Natural Laundry Detergent Liquid Soap, Lavender Scent, Vinegar Powered, 92 Fl Oz, 1 Count, Liquid Laundry Detergent, Laundry Soap",
            productRating = 4.6f,
            imageId = R.drawable.item_detergent,
        ),
        ProductInfo(
            // https://www.amazon.com/Cascade-Actionpacs-Dishwasher-Detergent-Lemon/dp/B07G28CM6P
            id = R.drawable.item_dishwash_detergent,
            storeName = "Cascade",
            storeInitials = "C",
            title = "Cascade Complete Dishwasher Pods, Dishwasher Detergent, Lemon Scent ActionPacs, Cleaning Supplies, 78 Count",
            productRating = 4.8f,
            imageId = R.drawable.item_dishwash_detergent,
        ),
        ProductInfo(
            // https://www.amazon.com/CATAN-Classic-Strategy-Players-Playtime/dp/B0DYK1ZH2D
            id = R.drawable.item_game_catan,
            storeName = "CATAN",
            storeInitials = "C",
            title = "CATAN Board Game (6th Edition) Trade, Build & Settle in The Classic Strategy Game for Family, Kids & Adults, Ages 10+, 3-4 Players, 60-90 Min Playtime",
            productRating = 4.8f,
            imageId = R.drawable.item_game_catan,
        ),
        ProductInfo(
            // https://www.amazon.com/Lookout-Games-Forest-Shuffle-Card/dp/B0C87WK67Z
            id = R.drawable.item_game_forest_shuffle,
            storeName = "Lookout",
            storeInitials = "L",
            title = "Lookout Games Forest Shuffle Card Game - Create an Ecological Paradise! Clever Strategy Game for Kids & Adults, Ages 10+, 2-5 Players, 60 Min Playtime, ECO-Friendly Packaging",
            productRating = 4.6f,
            imageId = R.drawable.item_game_forest_shuffle,
        ),
        ProductInfo(
            // https://www.amazon.com/Lost-Cities-Card-Game-Expedition/dp/B07KLWPHFF
            id = R.drawable.item_game_lost_cities,
            storeName = "Thames & Kosmos",
            storeInitials = "TK",
            title = "Lost Cities Card Game - with 6th Expedition - Thames & Kosmos Store - Designed By Reiner Knizia",
            productRating = 4.4f,
            imageId = R.drawable.item_game_lost_cities,
        ),
        ProductInfo(
            // https://www.amazon.com/Monopoly-Deal-Card-Game-English/dp/B0CQ59KDMT
            id = R.drawable.item_game_monopoly_deal,
            storeName = "Brand: Monopoly",
            storeInitials = "B:M",
            title = "Monopoly Deal Card Game - English Version",
            productRating = 4.4f,
            imageId = R.drawable.item_game_monopoly_deal,
        ),
        ProductInfo(
            // https://www.amazon.com/25th-Century-Games-POS187-Board
            id = R.drawable.item_game_ra,
            storeName = "25th Century Games",
            storeInitials = "25CG",
            title = "RA by 25th Century Games – Ancient Egyptian Theme, Auction and Set-Collection Board Game, for 2-5 Players and Ages 12+",
            productRating = 4.4f,
            imageId = R.drawable.item_game_ra,
        ),
        ProductInfo(
            // https://www.amazon.com/Method-Vetiver-Reusable-Aluminum-Bottle/dp/B09QXM15F6
            id = R.drawable.item_handsoap,
            storeName = "Method",
            storeInitials = "M",
            title = "Method Gel Hand Soap, Vetiver + Amber, Reusable Black Aluminum Bottle, Biodegradable Formula, 12 oz (Pack of 3)",
            productRating = 4.5f,
            imageId = R.drawable.item_handsoap,
        ),
        ProductInfo(
            // https://www.amazon.com/Bose-QuietComfort-Cancelling-Headphones-Bluetooth/dp/B0CCZ26B5V
            id = R.drawable.item_headphones,
            storeName = "Bose",
            storeInitials = "B",
            title = "Bose QuietComfort Bluetooth Headphones, Wireless Headphones with Active Over Ear Noise Cancelling and Mic, Deep Bass, Up to 24 Hours of Playtime, Black",
            productRating = 4.5f,
            imageId = R.drawable.item_headphones,
        ),
        ProductInfo(
            // https://www.amazon.com/Scotch-Brite-Heavy-Scrub-Sponges-Strong/dp/B0043P0E2M
            id = R.drawable.item_kitchen_sponge,
            storeName = "Scotch-Brite",
            storeInitials = "SB",
            title = "Scotch-Brite Heavy Duty Scrub Sponge, 9 Kitchen Sponges Designed for Tough Messes in the Kitchen that Last 3X Longer*, Best for Cast Iron Skillets and Outdoor Grills, Flexible, Durable, and Strong",
            productRating = 4.8f,
            imageId = R.drawable.item_kitchen_sponge,
        ),
        ProductInfo(
            // https://www.amazon.com/Jade-Leaf-Certified-Authentic-Antioxidants/dp/B00PFDH0K0
            id = R.drawable.item_matcha,
            storeName = "Jade Leaf Matcha",
            storeInitials = "JLM",
            title = "Jade Leaf Matcha Organic Culinary Grade Matcha Powder - Premium Second Harvest - Authentic Japanese Origin (3.53 Ounce Resealable Pouch)",
            productRating = 4.6f,
            imageId = R.drawable.item_matcha,
        ),
        ProductInfo(
            // https://www.amazon.com/Amazon-Basics-Reclosable-Sandwich-Storage/dp/B0CH929RS5
            id = R.drawable.item_sandwich_bags,
            storeName = "Amazon Basics",
            storeInitials = "AB",
            title = "Amazon Basics Reclosable Sandwich Double Zipper Storage Bags, 100 Count, Pack of 1",
            productRating = 4.7f,
            imageId = R.drawable.item_sandwich_bags,
        ),
        ProductInfo(
            // https://www.amazon.com/Zevia-Zero-Sugar-Soda-Ounce/dp/B0D6SM9SVM
            id = R.drawable.item_soda,
            storeName = "Zevia",
            storeInitials = "Z",
            title = "Zevia Zero Sugar Soda, Cola, 12 oz Cans (8-Pack) – Naturally Sweetened, Non-GMO Project Verified, Gluten-Free, Vegan",
            productRating = 4.1f,
            imageId = R.drawable.item_soda,
        ),
        ProductInfo(
            // https://www.amazon.com/Brita-Standard-Replacement-Pitchers-Dispensers/dp/B00008IHL8
            id = R.drawable.item_water_filter,
            storeName = "Brita",
            storeInitials = "B",
            title = "Brita Standard Water Filter for Pitchers and Dispensers, Reduces Copper, Cadmium and Mercury Impurities, Lasts Two Months or 40 Gallons, Includes 6 Filters for Pitchers",
            productRating = 4.8f,
            imageId = R.drawable.item_water_filter,
        ),
    )

    private val productsById = products.associateBy { it.id }

    fun getProductById(productId: Int): ProductInfo? = productsById[productId]

}
