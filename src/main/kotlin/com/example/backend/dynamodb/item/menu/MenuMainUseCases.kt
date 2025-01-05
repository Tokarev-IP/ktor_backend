package com.example.backend.dynamodb.item.menu

import com.example.Constants
import java.util.UUID

class MenuMainUseCases(
    private val menuMainAdapterRequestsInterface: MenuMainAdapterRequestsInterface,
) : MenuMainUseCasesInterface {

    override suspend fun createNewMenu(
        userId: String,
        amountOfSection: Int
    ): MenuMainData {
        val newSections = mutableListOf<SectionItemData>()
        for (i in 0 until amountOfSection) {
            val newSection = SectionItemData(
                sectionId = UUID.randomUUID().toString(),
                sectionName = "",
                isAvailable = false,
            )
            newSections.add(newSection)
        }
        val newMenuInfo = MenuInfoData(
            openingTime = OpeningTimeData(
                monday = "",
                tuesday = "",
                wednesday = "",
                thursday = "",
                friday = "",
                saturday = "",
                sunday = "",
            ),
            additionalInfo = "",
            address = "",
            imageUrl = null,
            menuName = "",
        )
        val newMenuMainData = MenuMainData(
            menuId = UUID.randomUUID().toString(),
            userId = userId,
            sections = newSections,
            info = newMenuInfo,
        )
        menuMainAdapterRequestsInterface.putMenuMainData(menuMainData = newMenuMainData)
        return newMenuMainData
    }

    override suspend fun updateMenuMainData(menuMainData: MenuMainData) {
        menuMainAdapterRequestsInterface.putMenuMainData(menuMainData = menuMainData)
    }

    override suspend fun getListOfMenuByUserId(userId: String): List<MenuMainData> {
        return menuMainAdapterRequestsInterface.getListOfMenuMainDataByUserId(userId = userId)
    }

    override suspend fun getMenuMainDataByMenuId(menuId: String): MenuMainData? {
        return menuMainAdapterRequestsInterface.getMenuMainDataByMenuId(menuId = menuId)
    }

    override suspend fun deleteMenu(menuId: String) {
        menuMainAdapterRequestsInterface.deleteMenuMenuDataByMenuId(menuId = menuId)
    }
}

interface MenuMainUseCasesInterface {

    private companion object: Constants() {
        private const val AMOUNT_SECTIONS = AMOUNT_OF_NEW_SECTIONS_OF_MENU
    }

    suspend fun createNewMenu(
        userId: String,
        amountOfSection: Int = AMOUNT_SECTIONS,
    ): MenuMainData

    suspend fun updateMenuMainData(menuMainData: MenuMainData)

    suspend fun getListOfMenuByUserId(userId: String): List<MenuMainData>

    suspend fun getMenuMainDataByMenuId(menuId: String): MenuMainData?

    suspend fun deleteMenu(menuId: String)
}