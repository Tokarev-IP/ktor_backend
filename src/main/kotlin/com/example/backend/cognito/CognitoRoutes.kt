package com.example.backend.cognito

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCognitoRoutes(
    cognitoUseCasesInterface: CognitoUseCasesInterface,
) {
    routing {
        route("user") {
            post("/create") {
                val authUserData = call.receive<AuthUserData>()
                cognitoUseCasesInterface.signUpWithEmailAndPassword(
                    password = authUserData.password,
                    email = authUserData.email,
                )
                call.respond(HttpStatusCode.OK, "User was created")
            }

            post("/login") {
                val authUserData = call.receive<AuthUserData>()
                val userTokenData = cognitoUseCasesInterface.loginWithEmailAndPassword(
                    email = authUserData.email,
                    password = authUserData.password,
                )
                call.respond(HttpStatusCode.OK, userTokenData)
            }

            post("/token/refresh") {
                val refreshToken = call.receiveText()
                if (refreshToken.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Refresh Token parameter is empty")
                } else {
                    val userTokenData = cognitoUseCasesInterface.loginWithRefreshToken(refreshToken = refreshToken)
                    call.respond(HttpStatusCode.OK, userTokenData)
                }
            }

            post("/confirmation/resend") {
                val email = call.receiveText()
                if (email.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Email parameter is empty")
                } else {
                    cognitoUseCasesInterface.resendConfirmationCode(email = email)
                    call.respond(HttpStatusCode.OK, "New code was sent")
                }
            }

            post("/confirmation/verify") {
                val verifyEmailData = call.receive<VerifyEmailData>()
                if (verifyEmailData.email.isBlank()
                    || verifyEmailData.confirmationCode.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Email or Verification Code parameter is empty")
                } else {
                    cognitoUseCasesInterface.verifyEmail(
                        email = verifyEmailData.email,
                        confirmationCode = verifyEmailData.confirmationCode,
                    )
                    call.respond(HttpStatusCode.OK, "Email was verified")
                }
            }

            authenticate("auth-bearer-cognito") {
                get("/info/{access_token}") {
                    val token = call.receive<MyToken>()
                    val response = cognitoUseCasesInterface.getUserByAccessToken(
                        accessToken = token.token
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }

}