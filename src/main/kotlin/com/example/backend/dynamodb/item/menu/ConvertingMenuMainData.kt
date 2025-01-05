package com.example.backend.dynamodb.item.menu

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ConvertingMenuMainData() : ConvertingMenuMainDataInterface {

    override suspend fun convertFromMapToMenuMainData(
        map: Map<String, AttributeValue>
    ): MenuMainData {
        return suspendCoroutine { continuation ->
            val cacheMenuMainData = CacheMenuMainData()

            map.forEach { (key, value) ->
                when (key) {
                    MenuMainDataAttributes.MENU_ID.key -> {
                        cacheMenuMainData.menuId = value.asS()
                    }

                    MenuMainDataAttributes.USER_ID.key -> {
                        cacheMenuMainData.userId = value.asS()
                    }

                    MenuMainDataAttributes.INFO.key -> {
                        val cacheMenuInfoData = CacheMenuInfoData()

                        val infoMap = value.asM()
                        infoMap.forEach { (keyInfo, valueInfo) ->
                            when (keyInfo) {

                                MenuInfoDataAttributes.OPENING_TIME.key -> {
                                    val cacheOpeningTimeData = CacheOpeningTimeData()

                                    val timeMap = valueInfo.asM()
                                    timeMap.forEach { (keyTime, valueTime) ->
                                        when (keyTime) {
                                            OpeningTimeDataAttribute.SUNDAY.key -> {
                                                cacheOpeningTimeData.sunday = valueTime.asS()
                                            }

                                            OpeningTimeDataAttribute.MONDAY.key -> {
                                                cacheOpeningTimeData.monday = valueTime.asS()
                                            }

                                            OpeningTimeDataAttribute.TUESDAY.key -> {
                                                cacheOpeningTimeData.tuesday = valueTime.asS()
                                            }

                                            OpeningTimeDataAttribute.WEDNESDAY.key -> {
                                                cacheOpeningTimeData.wednesday = valueTime.asS()
                                            }

                                            OpeningTimeDataAttribute.THURSDAY.key -> {
                                                cacheOpeningTimeData.thursday = valueTime.asS()
                                            }

                                            OpeningTimeDataAttribute.FRIDAY.key -> {
                                                cacheOpeningTimeData.friday = valueTime.asS()
                                            }

                                            OpeningTimeDataAttribute.SATURDAY.key -> {
                                                cacheOpeningTimeData.saturday = valueTime.asS()
                                            }
                                        }
                                    }
                                    cacheMenuInfoData.openingTime = cacheOpeningTimeData
                                }

                                MenuInfoDataAttributes.MENU_NAME.key -> {
                                    cacheMenuInfoData.menuName = valueInfo.asS()
                                }

                                MenuInfoDataAttributes.ADDITIONAL_INFO.key -> {
                                    cacheMenuInfoData.additionalInfo = valueInfo.asS()
                                }

                                MenuInfoDataAttributes.IMAGE_URL.key -> {
                                    cacheMenuInfoData.imageUrl = valueInfo.asSOrNull()
                                }

                                MenuInfoDataAttributes.ADDRESS.key -> {
                                    cacheMenuInfoData.address = valueInfo.asS()
                                }
                            }
                        }
                        cacheMenuMainData.info = cacheMenuInfoData
                    }

                    MenuMainDataAttributes.SECTIONS.key -> {
                        val cacheSectionItemDataList = mutableListOf<CacheSectionItemData>()

                        val sectionAttributeValueList = value.asL()
                        sectionAttributeValueList.forEach { attributeOfList ->
                            val cacheSectionItemData = CacheSectionItemData()

                            val itemMap = attributeOfList.asM()
                            itemMap.forEach { (keySection, valueSection) ->
                                when (keySection) {
                                    SectionItemDataAttributes.SECTION_ID.key -> {
                                        cacheSectionItemData.sectionId = valueSection.asS()
                                    }

                                    SectionItemDataAttributes.SECTION_NAME.key -> {
                                        cacheSectionItemData.sectionName = valueSection.asS()
                                    }

                                    SectionItemDataAttributes.IS_AVAILABLE.key -> {
                                        cacheSectionItemData.isAvailable = valueSection.asBool()
                                    }
                                }
                            }
                            cacheSectionItemDataList.add(cacheSectionItemData)
                        }
                        cacheMenuMainData.sections = cacheSectionItemDataList
                    }
                }
            }
            val menuMainData = cacheMenuMainData.transformToMenuMainData()
            continuation.resume(menuMainData)
        }
    }

    override suspend fun convertFromMenuMainDataToMap(
        menuMainData: MenuMainData,
    ): Map<String, AttributeValue> {
        return suspendCoroutine { continuation ->
            val menuInfoData = mutableMapOf<String, AttributeValue>()

            val openingTimeMapValues = mutableMapOf<String, AttributeValue>()
            openingTimeMapValues[OpeningTimeDataAttribute.SUNDAY.key] =
                AttributeValue.S(menuMainData.info.openingTime.sunday)
            openingTimeMapValues[OpeningTimeDataAttribute.MONDAY.key] =
                AttributeValue.S(menuMainData.info.openingTime.monday)
            openingTimeMapValues[OpeningTimeDataAttribute.TUESDAY.key] =
                AttributeValue.S(menuMainData.info.openingTime.tuesday)
            openingTimeMapValues[OpeningTimeDataAttribute.WEDNESDAY.key] =
                AttributeValue.S(menuMainData.info.openingTime.wednesday)
            openingTimeMapValues[OpeningTimeDataAttribute.THURSDAY.key] =
                AttributeValue.S(menuMainData.info.openingTime.thursday)
            openingTimeMapValues[OpeningTimeDataAttribute.FRIDAY.key] =
                AttributeValue.S(menuMainData.info.openingTime.friday)
            openingTimeMapValues[OpeningTimeDataAttribute.SATURDAY.key] =
                AttributeValue.S(menuMainData.info.openingTime.saturday)

            menuInfoData[MenuInfoDataAttributes.OPENING_TIME.key] = AttributeValue.M(openingTimeMapValues)
            menuInfoData[MenuInfoDataAttributes.MENU_NAME.key] = AttributeValue.S(menuMainData.info.menuName)
            menuInfoData[MenuInfoDataAttributes.ADDRESS.key] = AttributeValue.S(menuMainData.info.address)
            menuInfoData[MenuInfoDataAttributes.ADDITIONAL_INFO.key] =
                AttributeValue.S(menuMainData.info.additionalInfo)
            menuInfoData[MenuInfoDataAttributes.IMAGE_URL.key] =
                if (menuMainData.info.imageUrl == null) AttributeValue.Null(true)
                else AttributeValue.S(menuMainData.info.imageUrl)

            val sectionMapValuesList = mutableListOf<AttributeValue>()
            for (section in menuMainData.sections) {
                val sectionMapValues = mutableMapOf<String, AttributeValue>()
                sectionMapValues[SectionItemDataAttributes.SECTION_ID.key] = AttributeValue.S(section.sectionId)
                sectionMapValues[SectionItemDataAttributes.SECTION_NAME.key] = AttributeValue.S(section.sectionName)
                sectionMapValues[SectionItemDataAttributes.IS_AVAILABLE.key] = AttributeValue.Bool(section.isAvailable)
                sectionMapValuesList.add(AttributeValue.M(sectionMapValues))
            }

            val menuMainMapValues = mutableMapOf<String, AttributeValue>()
            menuMainMapValues[MenuMainDataAttributes.MENU_ID.key] = AttributeValue.S(menuMainData.menuId)
            menuMainMapValues[MenuMainDataAttributes.USER_ID.key] = AttributeValue.S(menuMainData.userId)
            menuMainMapValues[MenuMainDataAttributes.INFO.key] = AttributeValue.M(menuInfoData)
            menuMainMapValues[MenuMainDataAttributes.SECTIONS.key] = AttributeValue.L(sectionMapValuesList)

            continuation.resume(menuMainMapValues)
        }
    }

    override suspend fun convertFromListOfMapToListOfMenuMainData(
        mapList: List<Map<String, AttributeValue>>
    ): List<MenuMainData> {
        val menuMainDataList = mutableListOf<MenuMainData>()
        for (map in mapList) {
            val menuMainData = convertFromMapToMenuMainData(map = map)
            menuMainDataList.add(menuMainData)
        }
        return menuMainDataList
    }
}

interface ConvertingMenuMainDataInterface {
    suspend fun convertFromMapToMenuMainData(
        map: Map<String, AttributeValue>
    ): MenuMainData

    suspend fun convertFromMenuMainDataToMap(
        menuMainData: MenuMainData,
    ): Map<String, AttributeValue>

    suspend fun convertFromListOfMapToListOfMenuMainData(
        mapList: List<Map<String, AttributeValue>>
    ): List<MenuMainData>
}