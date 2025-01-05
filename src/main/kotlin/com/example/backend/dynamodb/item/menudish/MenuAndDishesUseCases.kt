package com.example.backend.dynamodb.item.menudish

import com.example.backend.dynamodb.item.dish.DishItemAdapterRequestsInterface
import com.example.backend.dynamodb.item.menu.MenuMainAdapterRequestsInterface

class MenuAndDishesUseCases(
    private val menuMainAdapterRequestsInterface: MenuMainAdapterRequestsInterface,
    private val dishItemAdapterRequestsInterface: DishItemAdapterRequestsInterface,
    private val convertingMenuAndDishesDataInterface: ConvertingMenuAndDishesDataInterface,
) : MenuAndDishesUseCasesInterface {

    override suspend fun getMenuAndDishes(menuId: String): MenuAndDishesData? {
        val menuMainData =
            menuMainAdapterRequestsInterface.getMenuMainDataByMenuId(menuId = menuId) ?: return null

        val dishItemDataList = dishItemAdapterRequestsInterface.getDishItemsByMenuId(menuId = menuId)

        val menuAndDishesData = convertingMenuAndDishesDataInterface.transformToMenuAndDishesData(
            menuMainData = menuMainData,
            dishItemDataList = dishItemDataList,
        )
        return menuAndDishesData
    }
}

interface MenuAndDishesUseCasesInterface {
    suspend fun getMenuAndDishes(menuId: String): MenuAndDishesData?
}