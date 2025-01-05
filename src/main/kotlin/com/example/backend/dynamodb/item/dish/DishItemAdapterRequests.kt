package com.example.backend.dynamodb.item.dish

import aws.sdk.kotlin.services.dynamodb.model.*
import com.example.Constants
import com.example.backend.dynamodb.item.DynamoDBRequestsInterface

class DishItemAdapterRequests(
    private val dynamoDBRequestsInterface: DynamoDBRequestsInterface,
    private val convertingDishDataInterface: ConvertingDishDataInterface,
) : DishItemAdapterRequestsInterface {

    override suspend fun putDishItem(
        dishItemData: DishItemData,
        dynamoDBTableName: String,
    ) {
        val itemMapValues = convertingDishDataInterface.convertFromDishItemDataToMap(dishItemData)
        val request = PutItemRequest {
            tableName = dynamoDBTableName
            item = itemMapValues
        }
        dynamoDBRequestsInterface.putItem(request = request)
    }

    override suspend fun getDishItem(
        dishId: String,
        dynamoDBTableName: String
    ): DishItemData? {
        val requestKey = mutableMapOf<String, AttributeValue>()
        requestKey[DishItemAttributes.DISH_ID_ATTRIBUTE.key] = AttributeValue.S(dishId)
        val request = GetItemRequest {
            tableName = dynamoDBTableName
            key = requestKey
        }
        val response = dynamoDBRequestsInterface.getItem(request = request)
        response?.let {
            val dishItemData = convertingDishDataInterface.convertFromMapToDishItemData(response)
            return dishItemData
        } ?: return null
    }

    override suspend fun getDishItemsByMenuId(
        menuId: String,
        dynamoDBTableName: String,
        menuIdIndexName: String,
    ): List<DishItemData> {
        val request = QueryRequest {
            tableName = dynamoDBTableName
            indexName = menuIdIndexName
            keyConditionExpression = "menu_id = :menu_id"
            expressionAttributeValues = mapOf(
                ":menu_id" to AttributeValue.S(menuId)
            )
        }
        val response = dynamoDBRequestsInterface.getItems(request = request)
        val dishItemDataList = convertingDishDataInterface.convertFromListOfMapToListOfDishItem(response)
        return dishItemDataList
    }

    override suspend fun getDishItemsBySectionIdAndMenuId(
        menuId: String,
        sectionId: String,
        dynamoDBTableName: String,
        menuIdSectionIdIndexName: String,
    ): List<DishItemData> {
        val request = QueryRequest {
            tableName = dynamoDBTableName
            indexName = menuIdSectionIdIndexName
            keyConditionExpression = "section_id = :section_id AND menu_id = :menu_id"
            expressionAttributeValues = mapOf(
                ":section_id" to AttributeValue.S(sectionId),
                ":menu_id" to AttributeValue.S(menuId)
            )
        }
        val response = dynamoDBRequestsInterface.getItems(request = request)
        val dishItemDataList = convertingDishDataInterface.convertFromListOfMapToListOfDishItem(response)
        return dishItemDataList
    }

    override suspend fun deleteDishItem(
        dishId: String,
        dynamoDBTableName: String
    ) {
        val requestKey = mutableMapOf<String, AttributeValue>()
        requestKey[DishItemAttributes.DISH_ID_ATTRIBUTE.key] = AttributeValue.S(dishId)
        val request = DeleteItemRequest {
            key = requestKey
            tableName = dynamoDBTableName
        }
        dynamoDBRequestsInterface.deleteItem(request = request)
    }
}

interface DishItemAdapterRequestsInterface {

    private companion object : Constants() {
        private const val TABLE_NAME = DYNAMO_DB_DISH_TABLE_NAME
        private const val MENU_ID_INDEX = DISH_TABLE_MENU_ID_INDEX
        private const val MENU_ID_SECTION_ID_INDEX = DISH_TABLE_MENU_ID_SECTION_ID_INDEX
    }

    suspend fun putDishItem(
        dishItemData: DishItemData,
        dynamoDBTableName: String = TABLE_NAME,
    )

    suspend fun getDishItem(
        dishId: String,
        dynamoDBTableName: String = TABLE_NAME,
    ): DishItemData?

    suspend fun getDishItemsByMenuId(
        menuId: String,
        dynamoDBTableName: String = TABLE_NAME,
        menuIdIndexName: String = MENU_ID_INDEX,
    ): List<DishItemData>

    suspend fun getDishItemsBySectionIdAndMenuId(
        menuId: String,
        sectionId: String,
        dynamoDBTableName: String = TABLE_NAME,
        menuIdSectionIdIndexName: String = MENU_ID_SECTION_ID_INDEX,
    ): List<DishItemData>

    suspend fun deleteDishItem(
        dishId: String,
        dynamoDBTableName: String = TABLE_NAME,
    )
}