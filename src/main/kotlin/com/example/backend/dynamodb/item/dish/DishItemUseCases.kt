package com.example.backend.dynamodb.item.dish

import com.example.Constants
import java.util.*

class DishItemUseCases(
    private val dishItemAdapterRequestsInterface: DishItemAdapterRequestsInterface
) : DishItemUseCasesInterface {

    override suspend fun createDishDataObject(
        createDishItemData: CreateDishItemData,
        newDishId: String,
        imageUrl: String
    ): DishItemData {
        val dishItemData = DishItemData(
            dishId = newDishId,
            menuId = createDishItemData.menuId,
            sectionId = createDishItemData.sectionId,
            dishInfo = DishInfoData(
                name = "",
                price = "",
                description = "",
                imageUrl = imageUrl,
                isInStock = false,
                isNew = true,
                isDiscount = false,
                discountPrice = "",
                additionalInfo = "",
                allergens = listOf("")
            )
        )
        dishItemAdapterRequestsInterface.putDishItem(dishItemData = dishItemData)
        return dishItemData
    }

    override suspend fun putDishDataObject(
        dishItemData: DishItemData,
    ) {
        dishItemAdapterRequestsInterface.putDishItem(dishItemData = dishItemData)
    }

    override suspend fun deleteDishItemObject(
        dishId: String,
    ) {
        dishItemAdapterRequestsInterface.deleteDishItem(dishId = dishId)
    }

    override suspend fun getDishItemListByMenuId(
        menuId: String,
    ): List<DishItemData> {
        return dishItemAdapterRequestsInterface.getDishItemsByMenuId(menuId = menuId)
    }

    override suspend fun getDishItemListByMenuIdAndSectionId(
        menuId: String,
        sectionId: String,
    ): List<DishItemData> {
        return dishItemAdapterRequestsInterface.getDishItemsBySectionIdAndMenuId(
            menuId = menuId,
            sectionId = sectionId,
        )
    }

    override suspend fun getDishItem(
        dishId: String,
    ): DishItemData? {
        return dishItemAdapterRequestsInterface.getDishItem(dishId = dishId)
    }
}

interface DishItemUseCasesInterface {

    private companion object: Constants() {
        private const val IMAGE_URL_BASE = S3_IMAGE_URL_BASE
    }

    suspend fun createDishDataObject(
        createDishItemData: CreateDishItemData,
        newDishId: String = UUID.randomUUID().toString(),
        imageUrl: String = "$IMAGE_URL_BASE$newDishId",
    ): DishItemData

    suspend fun putDishDataObject(dishItemData: DishItemData)

    suspend fun deleteDishItemObject(dishId: String)

    suspend fun getDishItemListByMenuId(menuId: String): List<DishItemData>

    suspend fun getDishItemListByMenuIdAndSectionId(
        menuId: String,
        sectionId: String,
    ): List<DishItemData>

    suspend fun getDishItem(dishId: String): DishItemData?
}