package com.example.backend.cognito

import com.example.Constants

class CognitoUseCases(
    private val cognitoRequestsInterface: CognitoRequestsInterface
) : CognitoUseCasesInterface {

    override suspend fun signUpWithEmailAndPassword(
        cognitoClientId: String,
        email: String,
        password: String,
    ) {
        cognitoRequestsInterface.signUpWithEmailAndPassword(
            cognitoClientId = cognitoClientId,
            userEmail = email,
            userPassword = password,
        )
    }

    override suspend fun loginWithEmailAndPassword(
        cognitoClientId: String,
        email: String,
        password: String,
    ): UserTokenData {
        val userTokenData = cognitoRequestsInterface.initiateAuthWithEmailAndPassword(
            cognitoClientId = cognitoClientId,
            userEmail = email,
            userPassword = password,
        )
        return userTokenData
    }

    override suspend fun loginWithRefreshToken(
        cognitoClientId: String,
        refreshToken: String,
    ): UserTokenData {
        val userTokenData = cognitoRequestsInterface.initiateAuthWithRefreshToken(
            cognitoClientId = cognitoClientId,
            userRefreshToken = refreshToken
        )
        return userTokenData
    }

    override suspend fun isAccessTokenValid(
        accessToken: String,
    ): Boolean {
        try {
            cognitoRequestsInterface.getUserByAccessToken(
                userAccessToken = accessToken
            )
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun getUserByAccessToken(
        accessToken: String,
    ): List<String> {
        val userResponse = cognitoRequestsInterface.getUserByAccessToken(
            userAccessToken = accessToken
        )
        return userResponse.userAttributes.map { it.toString() }
    }

    override suspend fun isEmailVerified(
        accessToken: String,
    ): Boolean {
        val userResponse = cognitoRequestsInterface.getUserByAccessToken(
            userAccessToken = accessToken
        )
        val isVerified = userResponse.userAttributes.find { it.name == "email_verified" }?.value
        return isVerified == "true"
    }

    override suspend fun verifyEmail(
        cognitoClientId: String,
        email: String,
        confirmationCode: String,
    ) {
        cognitoRequestsInterface.confirmSignUpWithEmail(
            cognitoClientId = cognitoClientId,
            userEmail = email,
            userConfirmationCode = confirmationCode,
        )
    }

    override suspend fun resendConfirmationCode(
        cognitoClientId: String,
        email: String,
    ) {
        cognitoRequestsInterface.resendConfirmationCodeWithEmail(
            cognitoClientId = cognitoClientId,
            userEmail = email,
        )
    }


}

interface CognitoUseCasesInterface {

    private companion object : Constants() {
        private const val CLIENT_ID = COGNITO_CLIENT_ID
    }

    suspend fun signUpWithEmailAndPassword(
        cognitoClientId: String = CLIENT_ID,
        email: String,
        password: String,
    )

    suspend fun loginWithEmailAndPassword(
        cognitoClientId: String = CLIENT_ID,
        email: String,
        password: String,
    ): UserTokenData

    suspend fun loginWithRefreshToken(
        cognitoClientId: String = CLIENT_ID,
        refreshToken: String,
    ): UserTokenData

    suspend fun isAccessTokenValid(
        accessToken: String,
    ): Boolean

    suspend fun getUserByAccessToken(
        accessToken: String,
    ): List<String>

    suspend fun isEmailVerified(
        accessToken: String,
    ): Boolean

    suspend fun verifyEmail(
        cognitoClientId: String = CLIENT_ID,
        email: String,
        confirmationCode: String,
    )

    suspend fun resendConfirmationCode(
        cognitoClientId: String = CLIENT_ID,
        email: String,
    )

}