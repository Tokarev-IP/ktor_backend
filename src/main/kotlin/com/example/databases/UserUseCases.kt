package com.example.databases

import java.util.*

class UserUseCases(
    private val userServiceInterface: UserServiceInterface
) : UserUseCasesInterface {

     private companion object {
         private const val ACCESS_TOKEN_TIME_LIMIT = 30000L // 1s == 1000L
    }

    override suspend fun registerUser(
        user: ExposedUser,
    ): String? {
        val userToken = userServiceInterface.checkUserByEmail(email = user.email)
        userToken?.let {
            return null
        } ?: run {
            val newToken = UUID.randomUUID().toString()
            val currentTime = System.currentTimeMillis()
            userServiceInterface.createNewUser(
                user = user,
                token = newToken,
                time = currentTime,
            )
            return newToken
        }
    }

    override suspend fun loginByEmailAndPassword(
        userLogin: ExposedUserLogin,
    ): String? {
        val currentTime = System.currentTimeMillis()
        val userToken = userServiceInterface.checkUserByEmailAndPassword(
            email = userLogin.email,
            password = userLogin.password,
        )
        userToken?.let {
            if (currentTime - userToken.tokenTime > ACCESS_TOKEN_TIME_LIMIT) {
                val newAccessToken = UUID.randomUUID().toString()
                userServiceInterface.updateTokenByEmail(
                    exposedUserToken = ExposedUserToken(
                        accessToken = newAccessToken,
                        tokenTime = currentTime
                    ),
                    email = userLogin.email,
                )
                return newAccessToken
            } else {
                return userToken.accessToken
            }
        } ?: return null
    }

    override suspend fun validateUserToken(
        token: String,
    ): String? {
        val currentTime = System.currentTimeMillis()
        val userToken = userServiceInterface.checkUserByToken(accessToken = token)
        userToken?.let {
            if (currentTime - userToken.tokenTime > ACCESS_TOKEN_TIME_LIMIT) {
                return null
            } else {
                return userToken.accessToken
            }
        } ?: return null
    }

    override suspend fun getAllUsers(): List<ExposedUser> {
        return userServiceInterface.readAllUsers()
    }
}

interface UserUseCasesInterface {
    suspend fun registerUser(
        user: ExposedUser,
    ): String?

    suspend fun loginByEmailAndPassword(
        userLogin: ExposedUserLogin,
    ): String?

    suspend fun validateUserToken(
        token: String,
    ): String?

    suspend fun getAllUsers(): List<ExposedUser>
}