package com.example.backend.dynamodb.item.dish

import kotlinx.serialization.Serializable

@Serializable
class CreateDishItemData(
    val menuId: String,
    val sectionId: String,
)

@Serializable
class DishItemData(
    override val menuId: String,
    override val dishId: String,
    override val sectionId: String,
    override val dishInfo: DishInfoData,
) : DishItemDataInterface

@Serializable
class DishInfoData(
    override val name: String,
    override val price: String,
    override val description: String,
    override val imageUrl: String?,
    override val isInStock: Boolean,
    override val isNew: Boolean,
    override val isDiscount: Boolean,
    override val discountPrice: String,
    override val additionalInfo: String,
    override val allergens: List<String>
): DishInfoDataInterface

enum class DishItemAttributes(val key: String) {
    MENU_ID_ATTRIBUTE("menu_id"),
    DISH_ID_ATTRIBUTE("dish_id"),
    SECTION_ID_ATTRIBUTE("section_id"),
    DISH_INFO_ATTRIBUTE("dish_info")
}

enum class DishInfoAttributes(val key: String) {
    NAME_ATTRIBUTE("name"),
    PRICE_ATTRIBUTE("price"),
    IMAGE_URL_ATTRIBUTE("image_url"),
    DESCRIPTION_ATTRIBUTE("description"),
    IS_IN_STOCK_ATTRIBUTE("is_in_stock"),
    IS_NEW_ATTRIBUTE("is_new"),
    IS_DISCOUNT_ATTRIBUTE("is_discount"),
    DISCOUNT_PRICE_ATTRIBUTE("discount_price"),
    ADDITIONAL_INFO_ATTRIBUTE("additional_info"),
    ALLERGENS_ATTRIBUTE("allergens")
}

class CacheDishItemData(
    override var menuId: String = "",
    override var dishId: String = "",
    override var sectionId: String = "",
    override var dishInfo: DishInfoDataInterface = CacheDishInfoData(),
) : DishItemDataInterface {

    fun convertToDishItemData(): DishItemData {
        return DishItemData(
            menuId = this.menuId,
            dishId = this.dishId,
            sectionId = this.sectionId,
            dishInfo = DishInfoData(
                name = dishInfo.name,
                price = dishInfo.price,
                description = dishInfo.description,
                imageUrl = dishInfo.imageUrl,
                isInStock = dishInfo.isInStock,
                isNew = dishInfo.isNew,
                isDiscount = dishInfo.isDiscount,
                discountPrice = dishInfo.discountPrice,
                additionalInfo = dishInfo.additionalInfo,
                allergens = dishInfo.allergens,
            )
        )
    }
}

class CacheDishInfoData(
    override var name: String = "",
    override var price: String = "",
    override var description: String = "",
    override var imageUrl: String? = null,
    override var isInStock: Boolean = false,
    override var isNew: Boolean = false,
    override var isDiscount: Boolean = false,
    override var discountPrice: String = "",
    override var additionalInfo: String = "",
    override var allergens: List<String> = listOf("")
) : DishInfoDataInterface

interface DishItemDataInterface {
    val menuId: String
    val dishId: String
    val sectionId: String
    val dishInfo: DishInfoDataInterface
}

interface DishInfoDataInterface {
    val name: String
    val price: String
    val description: String
    val imageUrl: String?
    val isInStock: Boolean
    val isNew: Boolean
    val isDiscount: Boolean
    val discountPrice: String
    val additionalInfo: String
    val allergens: List<String>
}