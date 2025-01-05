package com.example.backend.dynamodb.item

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.*
import com.example.Constants

class DynamoDBRequests : DynamoDBRequestsInterface {

    override suspend fun putItem(
        dbRegion: String,
        request: PutItemRequest
    ) {
        DynamoDbClient { region = dbRegion }.use { dynamodb ->
            dynamodb.putItem(request).attributes
        }
    }

    override suspend fun getItem(
        dbRegion: String,
        request: GetItemRequest
    ): Map<String, AttributeValue>? {
        DynamoDbClient { region = dbRegion }.use { dynamodb ->
            val response = dynamodb.getItem(request).item
            return response
        }
    }

    override suspend fun getItems(
        dbRegion: String,
        request: QueryRequest,
    ): List<Map<String, AttributeValue>> {
        DynamoDbClient { region = dbRegion }.use { dynamodb ->
            val response = dynamodb.query(request)
            return response.items ?: emptyList()
        }
    }

    override suspend fun deleteItem(
        dbRegion: String,
        request: DeleteItemRequest,
    ) {
        DynamoDbClient { region = dbRegion }.use { dynamodb ->
            dynamodb.deleteItem(request).attributes
        }
    }

    override suspend fun updateItem(
        dbRegion: String,
        request: UpdateItemRequest,
    ) {
        DynamoDbClient { region = dbRegion }.use { dynamodb ->
            dynamodb.updateItem(request).attributes
        }
    }

    override suspend fun scanItems(
        dbRegion: String,
        request: ScanRequest,
    ): List<Map<String, AttributeValue>> {
        DynamoDbClient { region = dbRegion }.use { dynamodb ->
            val response = dynamodb.scan(request)
            return response.items ?: emptyList()
        }
    }
}

interface DynamoDBRequestsInterface {

    private companion object : Constants() {
        private const val REGION = AWS_REGION
    }

    suspend fun putItem(
        dbRegion: String = REGION,
        request: PutItemRequest,
    )

    suspend fun getItem(
        dbRegion: String = REGION,
        request: GetItemRequest,
    ): Map<String, AttributeValue>?

    suspend fun getItems(
        dbRegion: String = REGION,
        request: QueryRequest,
    ): List<Map<String, AttributeValue>>

    suspend fun updateItem(
        dbRegion: String = REGION,
        request: UpdateItemRequest,
    )

    suspend fun deleteItem(
        dbRegion: String = REGION,
        request: DeleteItemRequest,
    )

    suspend fun scanItems(
        dbRegion: String = REGION,
        request: ScanRequest,
    ): List<Map<String, AttributeValue>>

}