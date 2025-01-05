package com.example.databases

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureUserDbRoutes(
    userUseCasesInterface: UserUseCasesInterface
) {
    authentication {
        bearer("auth-bearer-user") {
            realm = "Access to the '/' path"
            authenticate { tokenCredential ->
                val tokenFromDB = userUseCasesInterface.validateUserToken(tokenCredential.token)
                if (tokenFromDB == tokenCredential.token) {
                    UserIdPrincipal("Token is valid")
                } else {
                    null
                }
            }
        }
    }

    routing {
        post("/user/create") { // return token
            val user = call.receive<ExposedUser>()
            val newAccessToken = userUseCasesInterface.registerUser(user = user)
            newAccessToken?.let {
                call.respond(HttpStatusCode.OK, newAccessToken)
            } ?: call.respond(HttpStatusCode.Conflict, "User with this email address already exists")
        }

        post("/user/login") { //return token
            val loginData = call.receive<ExposedUserLogin>()
            val accessToken = userUseCasesInterface.loginByEmailAndPassword(userLogin = loginData)
            accessToken?.let {
                call.respond(HttpStatusCode.OK, accessToken)
            } ?: call.respond(HttpStatusCode.NotFound, "Email address or/and password are wrong")
        }

        get("/users") {
            val listOfUsers = userUseCasesInterface.getAllUsers()
            call.respond(HttpStatusCode.OK, listOfUsers)
        }

        get("/user/auth") {
            call.respond(HttpStatusCode.OK, "Auth is working")
        }
    }
}