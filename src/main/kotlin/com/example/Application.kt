package com.example

import com.example.backend.cognito.CognitoRequests
import com.example.backend.cognito.CognitoUseCases
import com.example.backend.cognito.configureCognitoRoutes
import com.example.backend.cognito.configureCognitoSecurity
import com.example.backend.dynamodb.item.DynamoDBRequests
import com.example.backend.dynamodb.item.dish.ConvertingDishData
import com.example.backend.dynamodb.item.dish.DishItemAdapterRequests
import com.example.backend.dynamodb.item.dish.DishItemUseCases
import com.example.backend.dynamodb.item.dish.configureDishItemRoutes
import com.example.backend.dynamodb.item.menu.ConvertingMenuMainData
import com.example.backend.dynamodb.item.menu.MenuMainAdapterRequests
import com.example.backend.dynamodb.item.menu.MenuMainUseCases
import com.example.backend.dynamodb.item.menu.configureMenuMainRoutes
import com.example.backend.dynamodb.item.menudish.ConvertingMenuAndDishesData
import com.example.backend.dynamodb.item.menudish.MenuAndDishesUseCases
import com.example.backend.dynamodb.item.menudish.configureMenuAndDishesRoutes
import com.example.backend.s3.`object`.S3ObjectAdapterRequests
import com.example.backend.s3.`object`.S3ObjectRequests
import com.example.backend.s3.`object`.image.S3ImageObjectUseCases
import com.example.backend.s3.`object`.image.configureS3ImageObjectRoutes
import com.example.databases.UserService
import com.example.databases.UserUseCases
import com.example.databases.configureDishDbRoutes
import com.example.databases.configureUserDbRoutes
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val cognitoUseCases = CognitoUseCases(CognitoRequests())
    val userUseCases = UserUseCases(UserService())
    val s3ObjectAdapterRequests = S3ObjectAdapterRequests(S3ObjectRequests())
    val s3ObjectUseCases = S3ImageObjectUseCases(s3ObjectAdapterRequests)
    val dynamoDBRequests = DynamoDBRequests()
    val dishItemAdapterRequests = DishItemAdapterRequests(
        dynamoDBRequestsInterface = dynamoDBRequests,
        convertingDishDataInterface = ConvertingDishData()
    )
    val dishItemUseCases = DishItemUseCases(dishItemAdapterRequests)
    val menuMainUseCases = MenuMainUseCases(
        MenuMainAdapterRequests(
            dynamoDBRequestsInterface = dynamoDBRequests,
            convertingMenuMainDataInterface = ConvertingMenuMainData()
        )
    )
    val menuAndDishesUseCases = MenuAndDishesUseCases(
        menuMainAdapterRequestsInterface = MenuMainAdapterRequests(
            dynamoDBRequestsInterface = dynamoDBRequests,
            convertingMenuMainDataInterface = ConvertingMenuMainData(),
        ),
        dishItemAdapterRequestsInterface = DishItemAdapterRequests(
            dynamoDBRequestsInterface = dynamoDBRequests,
            convertingDishDataInterface = ConvertingDishData(),
        ),
        convertingMenuAndDishesDataInterface = ConvertingMenuAndDishesData(),
    )

    configureRouting()
    configureSerialization()
    configureCognitoSecurity(cognitoUseCases)
    configureUserDbRoutes(userUseCases)
    configureDishDbRoutes()
    configureCognitoRoutes(cognitoUseCases)
    configureS3ImageObjectRoutes(s3ObjectUseCases)
    configureDishItemRoutes(dishItemUseCases)
    configureMenuMainRoutes(menuMainUseCases)
    configureMenuAndDishesRoutes(menuAndDishesUseCases)
}
