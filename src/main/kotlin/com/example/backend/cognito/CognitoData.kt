package com.example.backend.cognito

import kotlinx.serialization.Serializable

@Serializable
data class UserTokenData(
    val accessToken: String?,
    val refreshToken: String?,
    val idToken: String?,
)

@Serializable
data class MyToken(
    val token: String
)

@Serializable
data class AuthUserData(
    val email: String,
    val password: String,
)