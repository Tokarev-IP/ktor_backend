package com.example.backend.dynamodb.item.menudish

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureMenuAndDishesRoutes(
    menuAndDishesUseCasesInterface: MenuAndDishesUseCasesInterface
) {
    routing {
        route("/table/objects") {
            get("/{menu_id}") {
                val menuId = call.parameters["menu_id"]
                if (menuId.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest, "Menu ID parameter is empty")
                else {
                    menuAndDishesUseCasesInterface.getMenuAndDishes(menuId = menuId)
                        ?.let { menuAndDishesData: MenuAndDishesData ->
                            call.respond(HttpStatusCode.OK, menuAndDishesData)
                        } ?: call.respond(HttpStatusCode.NotFound, "Menu was not found")
                }
            }
        }
    }
}