package com.example.backend.cognito

import aws.sdk.kotlin.services.cognitoidentityprovider.CognitoIdentityProviderClient
import aws.sdk.kotlin.services.cognitoidentityprovider.model.*

class CognitoRequests : CognitoRequestsInterface {

    override suspend fun signUpWithEmailAndPassword(
        cognitoClientId: String,
        userEmail: String,
        userPassword: String,
        userPoolRegion: String,
    ) {
        val userAttrs =
            AttributeType {
                name = "email"
                value = userEmail
            }

        val userAttrsList = mutableListOf<AttributeType>().apply { add(userAttrs) }

        val signUpRequest =
            SignUpRequest {
                userAttributes = userAttrsList
                username = userEmail
                clientId = cognitoClientId
                password = userPassword
            }

        CognitoIdentityProviderClient { region = userPoolRegion }.use { identityProviderClient ->
            identityProviderClient.signUp(signUpRequest)
        }
    }

    override suspend fun resendConfirmationCodeWithEmail(
        cognitoClientId: String,
        userEmail: String,
        userPoolRegion: String,
    ) {
        val resendConfirmationCodeRequest =
            ResendConfirmationCodeRequest {
                username = userEmail
                clientId = cognitoClientId
            }

        CognitoIdentityProviderClient { region = userPoolRegion }.use { identityProviderClient ->
            identityProviderClient.resendConfirmationCode(resendConfirmationCodeRequest)
        }
    }

    override suspend fun confirmSignUpWithEmail(
        cognitoClientId: String,
        userEmail: String,
        userConfirmationCode: String,
        userPoolRegion: String,
    ) {
        val confirmSignUpRequest =
            ConfirmSignUpRequest {
                confirmationCode = userConfirmationCode
                username = userEmail
                clientId = cognitoClientId
            }

        CognitoIdentityProviderClient { region = userPoolRegion }.use { identityProviderClient ->
            identityProviderClient.confirmSignUp(confirmSignUpRequest)
        }
    }

    override suspend fun initiateAuthWithEmailAndPassword(
        cognitoClientId: String,
        userEmail: String,
        userPassword: String,
        userPoolRegion: String,
    ): UserTokenData {
        val userAuthParameters = mapOf<String, String>(
            "USERNAME" to userEmail,
            "PASSWORD" to userPassword
        )
        val initiateAuthRequest =
            InitiateAuthRequest {
                clientId = cognitoClientId
                authParameters = userAuthParameters
                authFlow = AuthFlowType.UserPasswordAuth
            }
        CognitoIdentityProviderClient { region = userPoolRegion }.use { identityProviderClient ->
            val authResponse = identityProviderClient.initiateAuth(initiateAuthRequest)
            val accessToken = authResponse.authenticationResult?.accessToken
            val refreshToken = authResponse.authenticationResult?.refreshToken
            val idToken = authResponse.authenticationResult?.idToken
            return UserTokenData(
                accessToken = accessToken,
                refreshToken = refreshToken,
                idToken = idToken,
            )
        }
    }

    override suspend fun initiateAuthWithRefreshToken(
        cognitoClientId: String,
        userRefreshToken: String,
        userPoolRegion: String,
    ): UserTokenData {
        val userAuthParameters = mapOf<String, String>(
            "REFRESH_TOKEN" to userRefreshToken
        )
        val initiateAuthRequest =
            InitiateAuthRequest {
                clientId = cognitoClientId
                authParameters = userAuthParameters
                authFlow = AuthFlowType.RefreshTokenAuth
            }
        CognitoIdentityProviderClient { region = userPoolRegion }.use { identityProviderClient ->
            val authResponse = identityProviderClient.initiateAuth(initiateAuthRequest)
            val accessToken = authResponse.authenticationResult?.accessToken
            val refreshToken = authResponse.authenticationResult?.refreshToken
            val idToken = authResponse.authenticationResult?.idToken
            return UserTokenData(
                accessToken = accessToken,
                refreshToken = refreshToken,
                idToken = idToken,
            )
        }
    }

    override suspend fun signOutGlobalWithAccessToken(
        userAccessToken: String,
        userPoolRegion: String,
    ) {
        val globalSignOutRequest =
            GlobalSignOutRequest {
                accessToken = userAccessToken
            }
        CognitoIdentityProviderClient { region = userPoolRegion }.use { identityProviderClient ->
            identityProviderClient.globalSignOut(globalSignOutRequest)
        }
    }

    override suspend fun getUserByAccessToken(
        userAccessToken: String,
        userPoolRegion: String,
    ): GetUserResponse {
        val getUserRequest =
            GetUserRequest {
                accessToken = userAccessToken
            }
        CognitoIdentityProviderClient { region = userPoolRegion }.use { identityProviderClient ->
            return identityProviderClient.getUser(getUserRequest)
        }
    }

}

interface CognitoRequestsInterface {

    private companion object {
        private const val REGION = "us-east-1"
    }

    suspend fun signUpWithEmailAndPassword(
        cognitoClientId: String,
        userEmail: String,
        userPassword: String,
        userPoolRegion: String = REGION,
    )

    suspend fun resendConfirmationCodeWithEmail(
        cognitoClientId: String,
        userEmail: String,
        userPoolRegion: String = REGION,
    )

    suspend fun confirmSignUpWithEmail(
        cognitoClientId: String,
        userEmail: String,
        userConfirmationCode: String,
        userPoolRegion: String = REGION,
    )

    suspend fun initiateAuthWithEmailAndPassword(
        cognitoClientId: String,
        userEmail: String,
        userPassword: String,
        userPoolRegion: String = REGION,
    ): UserTokenData

    suspend fun initiateAuthWithRefreshToken(
        cognitoClientId: String,
        userRefreshToken: String,
        userPoolRegion: String = REGION,
    ): UserTokenData

    suspend fun signOutGlobalWithAccessToken(
        userAccessToken: String,
        userPoolRegion: String = REGION,
    )

    suspend fun getUserByAccessToken(
        userAccessToken: String,
        userPoolRegion: String = REGION,
    ): GetUserResponse
}