package com.example.backend.dynamodb.item.menu

import aws.sdk.kotlin.services.dynamodb.model.*
import com.example.Constants
import com.example.backend.dynamodb.item.DynamoDBRequestsInterface

class MenuMainAdapterRequests(
    private val dynamoDBRequestsInterface: DynamoDBRequestsInterface,
    private val convertingMenuMainDataInterface: ConvertingMenuMainDataInterface,
) : MenuMainAdapterRequestsInterface {

    override suspend fun putMenuMainData(
        menuMainData: MenuMainData,
        dynamoDBTableName: String,
    ) {
        val itemMapValue = convertingMenuMainDataInterface.convertFromMenuMainDataToMap(menuMainData)
        val request = PutItemRequest {
            tableName = dynamoDBTableName
            item = itemMapValue
        }
        dynamoDBRequestsInterface.putItem(request = request)
    }

    override suspend fun getListOfMenuMainDataByUserId(
        userId: String,
        dynamoDBTableName: String,
        userIdIndexName: String,
    ): List<MenuMainData> {
        val request = QueryRequest {
            indexName = userIdIndexName
            tableName = dynamoDBTableName
            keyConditionExpression = "user_id = :user_id"
            expressionAttributeValues = mapOf(
                ":user_id" to AttributeValue.S(userId),
            )
        }
        val response = dynamoDBRequestsInterface.getItems(request = request)
        val menuIdData = convertingMenuMainDataInterface.convertFromListOfMapToListOfMenuMainData(mapList = response)
        return menuIdData
    }

    override suspend fun getMenuMainDataByMenuId(
        menuId: String,
        dynamoDBTableName: String
    ): MenuMainData? {
        val requestKey = mutableMapOf<String, AttributeValue>()
        requestKey[MenuMainDataAttributes.MENU_ID.key] = AttributeValue.S(menuId)
        val request = GetItemRequest {
            key = requestKey
            tableName = dynamoDBTableName
        }
        val response = dynamoDBRequestsInterface.getItem(request = request)
        response?.let {
            val menuMainData = convertingMenuMainDataInterface.convertFromMapToMenuMainData(map = response)
            return menuMainData
        } ?: return null
    }

    override suspend fun deleteMenuMenuDataByMenuId(
        menuId: String,
        dynamoDBTableName: String
    ) {
        val requestKey = mutableMapOf<String, AttributeValue>()
        requestKey[MenuMainDataAttributes.MENU_ID.key] = AttributeValue.S(menuId)
        val request = DeleteItemRequest {
            key = requestKey
            tableName = dynamoDBTableName
        }
        dynamoDBRequestsInterface.deleteItem(request = request)
    }
}

interface MenuMainAdapterRequestsInterface {

    private companion object {
        private const val TABLE_NAME = Constants.DYNAMO_DB_MENU_TABLE_NAME
        private const val USER_ID_INDEX = Constants.MENU_TABLE_USER_ID_MENU_ID_INDEX
    }

    suspend fun putMenuMainData(
        menuMainData: MenuMainData,
        dynamoDBTableName: String = TABLE_NAME,
    )

    suspend fun getListOfMenuMainDataByUserId(
        userId: String,
        dynamoDBTableName: String = TABLE_NAME,
        userIdIndexName: String = USER_ID_INDEX,
    ): List<MenuMainData>

    suspend fun getMenuMainDataByMenuId(
        menuId: String,
        dynamoDBTableName: String = TABLE_NAME,
    ): MenuMainData?

    suspend fun deleteMenuMenuDataByMenuId(
        menuId: String,
        dynamoDBTableName: String = TABLE_NAME,
    )
}