package com.zackjp.mockamazon.shared.data

import com.zackjp.mockamazon.shared.model.ProductCategory
import com.zackjp.mockamazon.shared.model.ProductInfo
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import com.zackjp.mockamazon.shared.R as SharedR

@Singleton
class ProductInMemoryDb(
    private val products: List<ProductInfo>,
) {

    @Inject
    constructor() : this(mockProducts)

    private companion object {
        private const val MAX_SIMILAR_PRODUCTS_RESULTS = 7

        private val mockProducts = listOf(
            ProductInfo(
                // https://www.amazon.com/Bellroy-Workpack-commuter-backpack-organization/dp/B0DK2ML5X3
                id = SharedR.drawable.item_backpack,
                storeName = "Bellroy",
                storeInitials = "B",
                title = "Bellroy Transit Workpack Pro (22L work bag/commuter backpack, fits 16” laptops; organization for travel and commutes)- Stone",
                productRating = 3.9f,
                imageId = SharedR.drawable.item_backpack,
                priceUSD = 249f,
                category = ProductCategory.BAGS_AND_CASES,
                discount = 0.17f,
            ),
            ProductInfo(
                // https://www.amazon.com/Cleanbear-Synthetic-Rubber-Gloves-Medium/dp/B01HJAKV3O
                id = SharedR.drawable.item_cleaning_gloves,
                storeName = "Cleanbear",
                storeInitials = "C",
                title = "Cleanbear Reusable Dish Washing Rubber Glove Set of 3, Household Cleaning Gloves for Washing",
                productRating = 4.6f,
                priceUSD = 12.99f,
                deliveryDate = LocalDate.now().plusDays(1),
                category = ProductCategory.HOUSEHOLD_SUPPLIES,
                imageId = SharedR.drawable.item_cleaning_gloves,
            ),
            ProductInfo(
                // https://www.amazon.com/Native-Deodorant-Aluminum-Parabens-Eucalyptus/dp/B07GB41NF9
                id = SharedR.drawable.item_deodorant,
                storeName = "Native",
                storeInitials = "N",
                title = "Native Aluminum-Free Clean Deodorant with Natural Derived Ingredients, 72HR odor control | Deodorant for Women and Men, With Baking Soda, Coconut Oil, Shea Butter | Eucalyptus & Mint",
                productRating = 4.4f,
                priceUSD = 12.99f,
                deliveryDate = LocalDate.now().plusDays(1),
                imageId = SharedR.drawable.item_deodorant,
                category = ProductCategory.TOILETRIES,
            ),
            ProductInfo(
                // https://www.amazon.com/Elements-Natural-Laundry-Detergent-Lavender/dp/B0BSJYJQGQ
                id = SharedR.drawable.item_detergent,
                storeName = "9 Elements",
                storeInitials = "9E",
                title = "9 Elements Natural Laundry Detergent Liquid Soap, Lavender Scent, Vinegar Powered, 92 Fl Oz, 1 Count, Liquid Laundry Detergent, Laundry Soap",
                productRating = 4.6f,
                imageId = SharedR.drawable.item_detergent,
                priceUSD = 15.99f,
                category = ProductCategory.HOUSEHOLD_SUPPLIES,
                discount = 0.12f,
            ),
            ProductInfo(
                // https://www.amazon.com/Cascade-Actionpacs-Dishwasher-Detergent-Lemon/dp/B07G28CM6P
                id = SharedR.drawable.item_dishwash_detergent,
                storeName = "Cascade",
                storeInitials = "C",
                title = "Cascade Complete Dishwasher Pods, Dishwasher Detergent, Lemon Scent ActionPacs, Cleaning Supplies, 78 Count",
                productRating = 4.8f,
                imageId = SharedR.drawable.item_dishwash_detergent,
                priceUSD = 20.99f,
                category = ProductCategory.HOUSEHOLD_SUPPLIES,
                discount = 0.13f,
            ),
            ProductInfo(
                // https://www.amazon.com/CATAN-Classic-Strategy-Players-Playtime/dp/B0DYK1ZH2D
                id = SharedR.drawable.item_game_catan,
                storeName = "CATAN",
                storeInitials = "C",
                title = "CATAN Board Game (6th Edition) Trade, Build & Settle in The Classic Strategy Game for Family, Kids & Adults, Ages 10+, 3-4 Players, 60-90 Min Playtime",
                productRating = 4.8f,
                imageId = SharedR.drawable.item_game_catan,
                priceUSD = 54.99f,
                category = ProductCategory.BOARD_GAMES,
            ),
            ProductInfo(
                // https://www.amazon.com/Lookout-Games-Forest-Shuffle-Card/dp/B0C87WK67Z
                id = SharedR.drawable.item_game_forest_shuffle,
                storeName = "Lookout",
                storeInitials = "L",
                title = "Lookout Games Forest Shuffle Card Game - Create an Ecological Paradise! Clever Strategy Game for Kids & Adults, Ages 10+, 2-5 Players, 60 Min Playtime, ECO-Friendly Packaging",
                productRating = 4.6f,
                imageId = SharedR.drawable.item_game_forest_shuffle,
                priceUSD = 29.99f,
                category = ProductCategory.BOARD_GAMES,
            ),
            ProductInfo(
                // https://www.amazon.com/Lost-Cities-Card-Game-Expedition/dp/B07KLWPHFF
                id = SharedR.drawable.item_game_lost_cities,
                storeName = "Thames & Kosmos",
                storeInitials = "TK",
                title = "Lost Cities Card Game - with 6th Expedition - Thames & Kosmos Store - Designed By Reiner Knizia",
                productRating = 4.4f,
                imageId = SharedR.drawable.item_game_lost_cities,
                priceUSD = 19.95f,
                category = ProductCategory.BOARD_GAMES,
            ),
            ProductInfo(
                // https://www.amazon.com/Monopoly-Deal-Card-Game-English/dp/B0CQ59KDMT
                id = SharedR.drawable.item_game_monopoly_deal,
                storeName = "Brand: Monopoly",
                storeInitials = "B:M",
                title = "Monopoly Deal Card Game - English Version",
                productRating = 4.4f,
                imageId = SharedR.drawable.item_game_monopoly_deal,
                deliveryDate = LocalDate.now().plusDays(1),
                category = ProductCategory.BOARD_GAMES,
                priceUSD = 13.99f,
            ),
            ProductInfo(
                // https://www.amazon.com/25th-Century-Games-POS187-Board/dp/B0BVMYN3ZS
                id = SharedR.drawable.item_game_ra,
                storeName = "25th Century Games",
                storeInitials = "25CG",
                title = "RA by 25th Century Games – Ancient Egyptian Theme, Auction and Set-Collection Board Game, for 2-5 Players and Ages 12+",
                productRating = 4.4f,
                imageId = SharedR.drawable.item_game_ra,
                priceUSD = 49.99f,
                category = ProductCategory.BOARD_GAMES,
            ),
            ProductInfo(
                // https://www.amazon.com/Method-Vetiver-Reusable-Aluminum-Bottle/dp/B09QXM15F6
                id = SharedR.drawable.item_handsoap,
                storeName = "Method",
                storeInitials = "M",
                title = "Method Gel Hand Soap, Vetiver + Amber, Reusable Black Aluminum Bottle, Biodegradable Formula, 12 oz (Pack of 3)",
                productRating = 4.5f,
                imageId = SharedR.drawable.item_handsoap,
                priceUSD = 25.07f,
                category = ProductCategory.TOILETRIES,
                discount = 0.11f,
            ),
            ProductInfo(
                // https://www.amazon.com/Bose-QuietComfort-Cancelling-Headphones-Bluetooth/dp/B0CCZ26B5V
                id = SharedR.drawable.item_headphones,
                storeName = "Bose",
                storeInitials = "B",
                title = "Bose QuietComfort Bluetooth Headphones, Wireless Headphones with Active Over Ear Noise Cancelling and Mic, Deep Bass, Up to 24 Hours of Playtime, Black",
                productRating = 4.5f,
                imageId = SharedR.drawable.item_headphones,
                priceUSD = 349.99f,
                category = ProductCategory.HEADPHONES,
                discount = 0.2f,
            ),
            ProductInfo(
                // https://www.amazon.com/Scotch-Brite-Heavy-Scrub-Sponges-Strong/dp/B0043P0E2M
                id = SharedR.drawable.item_kitchen_sponge,
                storeName = "Scotch-Brite",
                storeInitials = "SB",
                title = "Scotch-Brite Heavy Duty Scrub Sponge, 9 Kitchen Sponges Designed for Tough Messes in the Kitchen that Last 3X Longer*, Best for Cast Iron Skillets and Outdoor Grills, Flexible, Durable, and Strong",
                productRating = 4.8f,
                imageId = SharedR.drawable.item_kitchen_sponge,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 9.79f,
                category = ProductCategory.HOUSEHOLD_SUPPLIES,
                discount = 0.09f,
            ),
            ProductInfo(
                // https://www.amazon.com/Jade-Leaf-Certified-Authentic-Antioxidants/dp/B00PFDH0K0
                id = SharedR.drawable.item_matcha,
                storeName = "Jade Leaf Matcha",
                storeInitials = "JLM",
                title = "Jade Leaf Matcha Organic Culinary Grade Matcha Powder - Premium Second Harvest - Authentic Japanese Origin (3.53 Ounce Resealable Pouch)",
                productRating = 4.6f,
                imageId = SharedR.drawable.item_matcha,
                priceUSD = 25.99f,
                category = ProductCategory.BEVERAGES_TEA,
                discount = 0.10f,
            ),
            ProductInfo(
                // https://www.amazon.com/Amazon-Basics-Reclosable-Sandwich-Storage/dp/B0CH929RS5
                id = SharedR.drawable.item_sandwich_bags,
                storeName = "Amazon Basics",
                storeInitials = "AB",
                title = "Amazon Basics Reclosable Sandwich Double Zipper Storage Bags, 100 Count, Pack of 1",
                productRating = 4.7f,
                imageId = SharedR.drawable.item_sandwich_bags,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 3.37f,
                category = ProductCategory.HOUSEHOLD_SUPPLIES,
                discount = 0.07f,
            ),
            ProductInfo(
                // https://www.amazon.com/Zevia-Zero-Sugar-Soda-Ounce/dp/B0D6SM9SVM
                id = SharedR.drawable.item_soda,
                storeName = "Zevia",
                storeInitials = "Z",
                title = "Zevia Zero Sugar Soda, Cola, 12 oz Cans (8-Pack) – Naturally Sweetened, Non-GMO Project Verified, Gluten-Free, Vegan",
                productRating = 4.1f,
                imageId = SharedR.drawable.item_soda,
                priceUSD = 6.97f,
                category = ProductCategory.BEVERAGES_SOFT_DRINKS,
            ),
            ProductInfo(
                // https://www.amazon.com/Amazon-Fresh-Pretzel-Nugget-Peanut/dp/B0BDTBBHD1
                id = SharedR.drawable.item_snack_amazon_pbpretzels,
                storeName = "Amazon Grocery",
                storeInitials = "AG",
                title = "Amazon Grocery, Peanut Butter Filled Pretzels, 24 Oz (Previously Amazon Fresh, Packaging May Vary)",
                productRating = 4.5f,
                imageId = SharedR.drawable.item_snack_amazon_pbpretzels,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 5.43f,
                category = ProductCategory.SNACKS,
            ),
            ProductInfo(
                // https://www.amazon.com/Blue-Diamond-Almonds-Low-Sodium/dp/B07CTWYR92
                id = SharedR.drawable.item_snack_bluediamond_almonds,
                storeName = "Blue Diamond",
                storeInitials = "BD",
                title = "Blue Diamond Almonds Low Sodium Lightly Salted Snack Nuts, 40 Oz Resealable Bag (Pack of 1)",
                productRating = 4.7f,
                imageId = SharedR.drawable.item_snack_bluediamond_almonds,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 11.98f,
                category = ProductCategory.SNACKS,
            ),
            ProductInfo(
                // https://www.amazon.com/Dots-Homestyle-Pretzels-16-Ounce/dp/B0824B36NW
                id = SharedR.drawable.item_snack_dots_originalpretzels,
                storeName = "Dot's Homestyle Pretzels",
                storeInitials = "DHP",
                title = "Dot's Pretzels Original Seasoned Pretzel Twist Snack, 16oz Grocery Sized Bag",
                productRating = 4.7f,
                imageId = SharedR.drawable.item_snack_dots_originalpretzels,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 6.17f,
                category = ProductCategory.SNACKS,
            ),
            ProductInfo(
                // https://www.amazon.com/Larabar-Gluten-Peanut-Butter-Chocolate/dp/B003ZMXYMG
                id = SharedR.drawable.item_snack_larabar_pbchocolatechip,
                storeName = "LÄRABAR",
                storeInitials = "L",
                title = "Larabar Peanut Butter Chocolate Chip, Gluten Free Fruit & Nut Bar, 16 Ct",
                productRating = 4.6f,
                imageId = SharedR.drawable.item_snack_larabar_pbchocolatechip,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 17.25f,
                category = ProductCategory.SNACKS,
            ),
            ProductInfo(
                // https://www.amazon.com/Pop-Secret-Popcorn-Theater-Butter/dp/B07BMGW2WX
                id = SharedR.drawable.item_snack_popsecret_popcorn,
                storeName = "Pop Secret",
                storeInitials = "PS",
                title = "Pop Secret Microwave Popcorn, Movie Theater Butter Flavor, 1.75 Oz Snack Bags, (Pack of 12)",
                productRating = 4.6f,
                imageId = SharedR.drawable.item_snack_popsecret_popcorn,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 3.58f,
                category = ProductCategory.SNACKS,
            ),
            ProductInfo(
                // https://www.amazon.com/Sahale-Snacks-Glazed-Mix-Nut-Blend/dp/B07Z9SBTTC
                id = SharedR.drawable.item_snack_sahale_mixednuts,
                storeName = "Sahale Snacks",
                storeInitials = "S",
                title = "Sahale Snacks Glazed Mix Nut Blend Variety Pack, 1.5 Oz Grab & Go Bags (12 Total Packs) - Four Different Dry-Roasted Deluxe Mixed Nuts Blends Included - Non-GMO Kosher & Certified Gluten-Free Snacks",
                productRating = 4.6f,
                imageId = SharedR.drawable.item_snack_sahale_mixednuts,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 39.99f,
                category = ProductCategory.SNACKS,
            ),
            ProductInfo(
                // https://www.amazon.com/Wonderful-Pistachios-No-Shells-Calorie/dp/B07TCW23N9
                id = SharedR.drawable.item_snack_wonderful_pistachios,
                storeName = "Wonderful Pistachios",
                storeInitials = "WP",
                title = "Wonderful Pistachios No Shells, Roasted & Salted Nuts, 0.75 Ounce Bag (Pack of 14), Protein Snacks, Vegan Snacks, On-the-Go, Individual Snacks for Adults, Stocking Stuffers",
                productRating = 4.7f,
                imageId = SharedR.drawable.item_snack_wonderful_pistachios,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 12.10f,
                category = ProductCategory.SNACKS,
            ),
            ProductInfo(
                // https://www.amazon.com/adidas-Swift-Sneaker-White-Black/dp/B0BHPVXZGX
                id = SharedR.drawable.item_sneaker_adidas_swiftrun1,
                storeName = "adidas",
                storeInitials = "a",
                title = "adidas Men's Swift Run 1.0 Sneaker",
                productRating = 4.4f,
                imageId = SharedR.drawable.item_sneaker_adidas_swiftrun1,
                priceUSD = 90f,
                category = ProductCategory.SNEAKERS,
            ),
            ProductInfo(
                // https://www.amazon.com/adidas-Ubounce-Alphaskin-Sneaker-Black/dp/B0BZ5BV1L1
                id = SharedR.drawable.item_sneaker_adidas_ubounce,
                storeName = "adidas",
                storeInitials = "a",
                title = "adidas Men's Ubounce Dna Running Shoes",
                productRating = 4.6f,
                imageId = SharedR.drawable.item_sneaker_adidas_ubounce,
                priceUSD = 100f,
                category = ProductCategory.SNEAKERS,
            ),
            ProductInfo(
                // https://www.amazon.com/Allbirds-Tree-Dasher-2/dp/B0FRNP3GBM
                id = SharedR.drawable.item_sneaker_allbirds_treedasher2,
                storeName = "Allbirds",
                storeInitials = "A",
                title = "Allbirds Men's Tree Dasher 2, Active Running & Walking Sneakers",
                productRating = 4.2f,
                imageId = SharedR.drawable.item_sneaker_allbirds_treedasher2,
                priceUSD = 135f,
                category = ProductCategory.SNEAKERS,
            ),
            ProductInfo(
                // https://www.amazon.com/Allbirds-Tree-Glider/dp/B0DQB1WWRS
                id = SharedR.drawable.item_sneaker_allbirds_treeglider,
                storeName = "Allbirds",
                storeInitials = "A",
                title = "Allbirds Men's Tree Glider, Everyday Active Sneakers",
                productRating = 4.4f,
                imageId = SharedR.drawable.item_sneaker_allbirds_treeglider,
                priceUSD = 135f,
                category = ProductCategory.SNEAKERS,
            ),
            ProductInfo(
                // https://www.amazon.com/Under-Armour-Mens-Charged-Assert-9/dp/B08CFTM283
                id = SharedR.drawable.item_sneaker_underarmour_chargedassert9,
                storeName = "Under Armour",
                storeInitials = "UA",
                title = "Under Armour Men's Charged Assert 9 Running Shoe",
                productRating = 4.6f,
                imageId = SharedR.drawable.item_sneaker_underarmour_chargedassert9,
                priceUSD = 70f,
                category = ProductCategory.SNEAKERS,
            ),
            ProductInfo(
                // https://www.amazon.com/Feethit-Slipon/dp/B08HLSLV6B
                id = SharedR.drawable.item_sneaker_feethit_slipon,
                storeName = "Feethit",
                storeInitials = "F",
                title = "Feethit Mens Slip On Running Shoes Breathable Lightweight Comfortable Fashion Non Slip Sneakers for Men\n",
                productRating = 4.1f,
                imageId = SharedR.drawable.item_sneaker_feethit_slipon,
                priceUSD = 35.99f,
                category = ProductCategory.SNEAKERS,
                discount = .2f,
            ),
            ProductInfo(
                // https://www.amazon.com/Brita-Standard-Replacement-Pitchers-Dispensers/dp/B00008IHL8
                id = SharedR.drawable.item_water_filter,
                storeName = "Brita",
                storeInitials = "B",
                title = "Brita Standard Water Filter for Pitchers and Dispensers, Reduces Copper, Cadmium and Mercury Impurities, Lasts Two Months or 40 Gallons, Includes 6 Filters for Pitchers",
                productRating = 4.8f,
                imageId = SharedR.drawable.item_water_filter,
                deliveryDate = LocalDate.now().plusDays(1),
                priceUSD = 28.49f,
                category = ProductCategory.HOUSEHOLD_SUPPLIES,
            ),
        )
    }

    private val productsById = products.associateBy { it.id }

    fun getProductById(productId: Int): ProductInfo? = productsById[productId]

    fun getSimilarProducts(productId: Int): List<ProductInfo> {
        val productInfo = getProductById(productId)
        if (productInfo == null) return emptyList()
        if (productInfo.category == ProductCategory.UNKNOWN) return emptyList()

        return products.asSequence()
            .filter { it.category == productInfo.category && it.id != productId }
            .take(MAX_SIMILAR_PRODUCTS_RESULTS)
            .toList()
    }

    fun findProducts(searchString: String): List<ProductInfo> {
        val uppercaseSearchString = searchString.uppercase()

        val categoryMatcher: (ProductInfo) -> Boolean = {
            it.category != ProductCategory.UNKNOWN
            && it.category.name.contains(uppercaseSearchString)
        }

        val titleMatcher: (ProductInfo) -> Boolean = {
            it.title.uppercase().contains(uppercaseSearchString)
        }

        return products.filter {
            categoryMatcher(it) || titleMatcher(it)
        }
    }

}
