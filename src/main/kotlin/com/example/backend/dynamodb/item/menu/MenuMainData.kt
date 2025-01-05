package com.example.backend.dynamodb.item.menu

import kotlinx.serialization.Serializable

@Serializable
class CreateMenuMainData(
    val userId: String,
)

@Serializable
class MenuMainData(
    override val menuId: String,
    override val userId: String,
    override val sections: List<SectionItemData>,
    override val info: MenuInfoData,
) : MenuMainDataInterface

@Serializable
class SectionItemData(
    override val sectionId: String,
    override val sectionName: String,
    override val isAvailable: Boolean,
) : SectionItemDataInterface

@Serializable
class OpeningTimeData(
    override val monday: String,
    override val tuesday: String,
    override val wednesday: String,
    override val thursday: String,
    override val friday: String,
    override val saturday: String,
    override val sunday: String,
) : OpeningTimeDataInterface

@Serializable
class MenuInfoData(
    override val openingTime: OpeningTimeData,
    override val additionalInfo: String,
    override val address: String,
    override val imageUrl: String?,
    override val menuName: String,
) : MenuInfoDataInterface

class CacheMenuMainData(
    override var menuId: String = "",
    override var userId: String = "",
    override var sections: List<CacheSectionItemData> = emptyList(),
    override var info: MenuInfoDataInterface = CacheMenuInfoData()
) : MenuMainDataInterface {

    fun transformToMenuMainData(): MenuMainData {
        val sectionItemDataList = mutableListOf<SectionItemData>()
        for (section in sections) {
            val sectionItemData = SectionItemData(
                sectionId = section.sectionId,
                sectionName = section.sectionName,
                isAvailable = section.isAvailable,
            )
            sectionItemDataList.add(sectionItemData)
        }

        val menuInfoData = MenuInfoData(
            openingTime = OpeningTimeData(
                monday = info.openingTime.monday,
                tuesday = info.openingTime.tuesday,
                wednesday = info.openingTime.wednesday,
                thursday = info.openingTime.thursday,
                friday = info.openingTime.friday,
                saturday = info.openingTime.saturday,
                sunday = info.openingTime.sunday,
            ),
            additionalInfo = info.additionalInfo,
            address = info.address,
            imageUrl = info.imageUrl,
            menuName = info.menuName,
        )

        val menuMainData = MenuMainData(
            menuId = this.menuId,
            userId = this.userId,
            sections = sectionItemDataList,
            info = menuInfoData,
        )

        return menuMainData
    }
}

class CacheOpeningTimeData(
    override var monday: String = "",
    override var tuesday: String = "",
    override var wednesday: String = "",
    override var thursday: String = "",
    override var friday: String = "",
    override var saturday: String = "",
    override var sunday: String = "",
) : OpeningTimeDataInterface

class CacheSectionItemData(
    override var sectionId: String = "",
    override var sectionName: String = "",
    override var isAvailable: Boolean = false,
) : SectionItemDataInterface

class CacheMenuInfoData(
    override var openingTime: OpeningTimeDataInterface = CacheOpeningTimeData(),
    override var additionalInfo: String = "",
    override var address: String = "",
    override var imageUrl: String? = null,
    override var menuName: String = ""
) : MenuInfoDataInterface

enum class MenuMainDataAttributes(val key: String) {
    MENU_ID("menu_id"),
    USER_ID("user_id"),
    SECTIONS("sections"),
    INFO("info"),
}

enum class OpeningTimeDataAttribute(val key: String) {
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday"),
    SUNDAY("sunday");
}

enum class SectionItemDataAttributes(val key: String) {
    SECTION_ID("section_id"),
    SECTION_NAME("section_name"),
    IS_AVAILABLE("is_available"),
}

enum class MenuInfoDataAttributes(val key: String) {
    ADDITIONAL_INFO("additional_info"),
    OPENING_TIME("opening_time"),
    ADDRESS("address"),
    IMAGE_URL("image_url"),
    MENU_NAME("menu_name"),
}

interface MenuMainDataInterface {
    val menuId: String
    val userId: String
    val sections: List<SectionItemDataInterface>
    val info: MenuInfoDataInterface
}

interface SectionItemDataInterface {
    val sectionId: String
    val sectionName: String
    val isAvailable: Boolean
}

interface OpeningTimeDataInterface {
    val monday: String
    val tuesday: String
    val wednesday: String
    val thursday: String
    val friday: String
    val saturday: String
    val sunday: String
}

interface MenuInfoDataInterface {
    val openingTime: OpeningTimeDataInterface
    val additionalInfo: String
    val address: String
    val imageUrl: String?
    val menuName: String
}