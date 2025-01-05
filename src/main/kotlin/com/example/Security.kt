package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.security.interfaces.RSAPublicKey

//fun Application.configureJWT() {
//    authentication {
//        jwt("auth-jwt") {
//            verifier(
//                JWT
//                    .require(Algorithm.RSA256(getPublicKey(), null))
//                    .withIssuer("https://cognito-idp.us-east-1.amazonaws.com/us-east-1_examplePoolId")
//                    .build()
//            )
//            validate { credential ->
//                if (credential.payload.getClaim("sub").asString() != null) {
//                    JWTPrincipal(credential.payload)
//                } else null
//            }
//        }
//    }
//}
//
//private fun getPublicKey(): RSAPublicKey? {
//    // Загрузите публичный ключ из Cognito JWKS
//}
