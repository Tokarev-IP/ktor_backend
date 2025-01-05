package com.example.backend.dynamodb.item.menudish

import com.example.backend.dynamodb.item.dish.DishInfoData
import com.example.backend.dynamodb.item.menu.MenuInfoData
import kotlinx.serialization.Serializable

@Serializable
class MenuAndDishesData(
    val menuId: String,
    val sectionsAndDishes: List<SectionDishData>,
    val info: MenuInfoData,
)

@Serializable
class SectionDishData(
    val sectionName: String,
    val dishInfoList: List<DishInfoData>,
)