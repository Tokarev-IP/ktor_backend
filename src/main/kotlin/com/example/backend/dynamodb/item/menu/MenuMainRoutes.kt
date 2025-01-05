package com.example.backend.dynamodb.item.menu

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureMenuMainRoutes(
    menuMainUseCasesInterface: MenuMainUseCasesInterface,
) {
    routing {
        route("/table/object/menu") {
            post {
                val createMenuMainData = call.receive<CreateMenuMainData>()
                if (createMenuMainData.userId.isBlank())
                    call.respond(HttpStatusCode.BadRequest, "User ID is incorrect")
                menuMainUseCasesInterface.createNewMenu(userId = createMenuMainData.userId)
                call.respond(HttpStatusCode.OK, "Menu was created")
            }

            put {
                val menuMainData = call.receive<MenuMainData>()
                menuMainUseCasesInterface.updateMenuMainData(menuMainData = menuMainData)
                call.respond(HttpStatusCode.OK, "Menu was updated")
            }

            get("/owner/{user_id}") {
                val userId = call.parameters["user_id"]
                if (userId.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest, "The User ID is incorrect")
                else {
                    val response = menuMainUseCasesInterface.getListOfMenuByUserId(userId = userId)
                    call.respond(HttpStatusCode.OK, response)
                }
            }

            get("/{menu_id}") {
                val menuId = call.parameters["menu_id"]
                if (menuId.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest, "Menu ID parameter is empty")
                else {
                    menuMainUseCasesInterface.getMenuMainDataByMenuId(menuId = menuId)
                        ?.let { menuMainData: MenuMainData ->
                            call.respond(HttpStatusCode.OK, menuMainData)
                        } ?: call.respond(HttpStatusCode.NotFound, "The data was not found")
                }
            }

            delete("/{menu_id}") {
                val menuId = call.parameters["menu_id"]
                if (menuId.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest, "Menu ID parameter is e,pty")
                else {
                    menuMainUseCasesInterface.deleteMenu(menuId = menuId)
                    call.respond(HttpStatusCode.OK, "The menu was deleted")
                }
            }
        }
    }
}