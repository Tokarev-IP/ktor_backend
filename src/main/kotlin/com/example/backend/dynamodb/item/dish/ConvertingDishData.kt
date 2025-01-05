package com.example.backend.dynamodb.item.dish

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ConvertingDishData : ConvertingDishDataInterface {

    override suspend fun convertFromMapToDishItemData(
        map: Map<String, AttributeValue>
    ): DishItemData {
        return suspendCoroutine { continuation ->
            val cacheDishItemData = CacheDishItemData()
            map.forEach { (key, value) ->
                when (key) {
                    DishItemAttributes.MENU_ID_ATTRIBUTE.key -> {
                        cacheDishItemData.menuId = value.asS()
                    }

                    DishItemAttributes.DISH_ID_ATTRIBUTE.key -> {
                        cacheDishItemData.dishId = value.asS()
                    }

                    DishItemAttributes.SECTION_ID_ATTRIBUTE.key -> {
                        cacheDishItemData.sectionId = value.asS()
                    }

                    DishItemAttributes.DISH_INFO_ATTRIBUTE.key -> {
                        val cacheDishInfoData = CacheDishInfoData()
                        val dishInfoMap = value.asM()
                        dishInfoMap.forEach { (keyInfo, valueInfo) ->
                            when (keyInfo) {
                                DishInfoAttributes.ADDITIONAL_INFO_ATTRIBUTE.key -> {
                                    cacheDishInfoData.additionalInfo = valueInfo.asS()
                                }

                                DishInfoAttributes.NAME_ATTRIBUTE.key -> {
                                    cacheDishInfoData.name = valueInfo.asS()
                                }

                                DishInfoAttributes.PRICE_ATTRIBUTE.key -> {
                                    cacheDishInfoData.price = valueInfo.asS()
                                }

                                DishInfoAttributes.IS_NEW_ATTRIBUTE.key -> {
                                    cacheDishInfoData.isNew = valueInfo.asBool()
                                }

                                DishInfoAttributes.DESCRIPTION_ATTRIBUTE.key -> {
                                    cacheDishInfoData.description = valueInfo.asS()
                                }

                                DishInfoAttributes.ALLERGENS_ATTRIBUTE.key -> {
                                    cacheDishInfoData.allergens = valueInfo.asSs()
                                }

                                DishInfoAttributes.DISCOUNT_PRICE_ATTRIBUTE.key -> {
                                    cacheDishInfoData.discountPrice = valueInfo.asS()
                                }

                                DishInfoAttributes.IMAGE_URL_ATTRIBUTE.key -> {
                                    cacheDishInfoData.imageUrl = valueInfo.asSOrNull()
                                }

                                DishInfoAttributes.IS_DISCOUNT_ATTRIBUTE.key -> {
                                    cacheDishInfoData.isDiscount = valueInfo.asBool()
                                }

                                DishInfoAttributes.IS_IN_STOCK_ATTRIBUTE.key -> {
                                    cacheDishInfoData.isInStock = valueInfo.asBool()
                                }
                            }
                        }
                        cacheDishItemData.dishInfo = cacheDishInfoData
                    }
                }
            }
            val dishItemData = cacheDishItemData.convertToDishItemData()
            continuation.resume(dishItemData)
        }
    }

    override suspend fun convertFromListOfMapToListOfDishItem(
        mapList: List<Map<String, AttributeValue>>
    ): List<DishItemData> {
        val dishItemList = mutableListOf<DishItemData>()
        mapList.forEach { item ->
            val dishItem = convertFromMapToDishItemData(item)
            dishItemList.add(dishItem)
        }
        return dishItemList
    }

    override suspend fun convertFromDishItemDataToMap(
        dishItemData: DishItemData
    ): Map<String, AttributeValue> {
        return suspendCoroutine { continuation ->

            val dishInfoMapValues = mutableMapOf<String, AttributeValue>()
            dishInfoMapValues[DishInfoAttributes.ADDITIONAL_INFO_ATTRIBUTE.key] =
                AttributeValue.S(dishItemData.dishInfo.additionalInfo)
            dishInfoMapValues[DishInfoAttributes.NAME_ATTRIBUTE.key] = AttributeValue.S(dishItemData.dishInfo.name)
            dishInfoMapValues[DishInfoAttributes.PRICE_ATTRIBUTE.key] = AttributeValue.S(dishItemData.dishInfo.price)
            dishInfoMapValues[DishInfoAttributes.DESCRIPTION_ATTRIBUTE.key] =
                AttributeValue.S(dishItemData.dishInfo.description)
            dishInfoMapValues[DishInfoAttributes.DISCOUNT_PRICE_ATTRIBUTE.key] =
                AttributeValue.S(dishItemData.dishInfo.discountPrice)
            dishInfoMapValues[DishInfoAttributes.IS_NEW_ATTRIBUTE.key] =
                AttributeValue.Bool(dishItemData.dishInfo.isNew)
            dishInfoMapValues[DishInfoAttributes.IMAGE_URL_ATTRIBUTE.key] =
                if (dishItemData.dishInfo.imageUrl == null) AttributeValue.Bool(true)
                else AttributeValue.S(dishItemData.dishInfo.imageUrl)
            dishInfoMapValues[DishInfoAttributes.IS_IN_STOCK_ATTRIBUTE.key] =
                AttributeValue.Bool(dishItemData.dishInfo.isInStock)
            dishInfoMapValues[DishInfoAttributes.IS_DISCOUNT_ATTRIBUTE.key] =
                AttributeValue.Bool(dishItemData.dishInfo.isDiscount)
            dishInfoMapValues[DishInfoAttributes.ALLERGENS_ATTRIBUTE.key] =
                AttributeValue.Ss(dishItemData.dishInfo.allergens)

            val dishItemMapValues = mutableMapOf<String, AttributeValue>()
            dishItemMapValues[DishItemAttributes.DISH_ID_ATTRIBUTE.key] = AttributeValue.S(dishItemData.dishId)
            dishItemMapValues[DishItemAttributes.MENU_ID_ATTRIBUTE.key] = AttributeValue.S(dishItemData.menuId)
            dishItemMapValues[DishItemAttributes.SECTION_ID_ATTRIBUTE.key] = AttributeValue.S(dishItemData.sectionId)
            dishItemMapValues[DishItemAttributes.DISH_INFO_ATTRIBUTE.key] = AttributeValue.M(dishInfoMapValues)

            continuation.resume(dishItemMapValues)
        }
    }
}

interface ConvertingDishDataInterface {

    suspend fun convertFromMapToDishItemData(
        map: Map<String, AttributeValue>
    ): DishItemData

    suspend fun convertFromListOfMapToListOfDishItem(
        mapList: List<Map<String, AttributeValue>>
    ): List<DishItemData>

    suspend fun convertFromDishItemDataToMap(
        dishItemData: DishItemData
    ): Map<String, AttributeValue>
}