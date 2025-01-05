package com.example.backend.dynamodb.item.menudish

import com.example.backend.dynamodb.item.dish.DishItemData
import com.example.backend.dynamodb.item.menu.MenuMainData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ConvertingMenuAndDishesData : ConvertingMenuAndDishesDataInterface {

    override suspend fun transformToMenuAndDishesData(
        menuMainData: MenuMainData,
        dishItemDataList: List<DishItemData>
    ): MenuAndDishesData {
        return suspendCoroutine { continuation ->
            val sectionDishDataList = mutableListOf<SectionDishData>()
            for (section in menuMainData.sections) {
                val dishInfoDataList = dishItemDataList
                    .filter { dishItem ->
                        dishItem.sectionId == section.sectionId
                    }
                    .map {
                        it.dishInfo
                    }
                if (dishInfoDataList.isNotEmpty())
                    sectionDishDataList.add(
                        SectionDishData(
                            sectionName = section.sectionName,
                            dishInfoList = dishInfoDataList,
                        )
                    )
            }
            val menuAndDishesData = MenuAndDishesData(
                menuId = menuMainData.menuId,
                sectionsAndDishes = sectionDishDataList,
                info = menuMainData.info,
            )
            continuation.resume(menuAndDishesData)
        }
    }
}

interface ConvertingMenuAndDishesDataInterface {
    suspend fun transformToMenuAndDishesData(
        menuMainData: MenuMainData,
        dishItemDataList: List<DishItemData>
    ): MenuAndDishesData
}