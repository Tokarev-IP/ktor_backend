package com.example.backend.cognito

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureCognitoSecurity(
    cognitoUseCasesInterface: CognitoUseCasesInterface,
){
    authentication {
        bearer("auth-bearer-cognito") {
            realm = "Access to the '/user' path"
            authenticate { tokenCredential ->
                val isAccessTokenValid = cognitoUseCasesInterface.isAccessTokenValid(tokenCredential.token)
                if (isAccessTokenValid) {
                    UserIdPrincipal("Token is valid")
                } else {
                    null
                }
            }
        }
    }
}