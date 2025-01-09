package com.example.backend.cognito

import kotlinx.serialization.Serializable

@Serializable
class UserTokenData(
    val accessToken: String?,
    val refreshToken: String?,
    val idToken: String?,
)

@Serializable
class MyToken(
    val token: String
)

@Serializable
class AuthUserData(
    val email: String,
    val password: String,
)

@Serializable
class VerifyEmailData(
    val email: String,
    val confirmationCode: String,
)