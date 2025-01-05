package com.example.backend.dynamodb.item.dish

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureDishItemRoutes(
    dishItemUseCasesInterface: DishItemUseCasesInterface
) {
    routing {
        route("/table/object/dish") {
            post {
                val createDishItemData = call.receive<CreateDishItemData>()
                if (createDishItemData.sectionId.isBlank() || createDishItemData.menuId.isBlank())
                    call.respond(HttpStatusCode.BadRequest, "Bad request")
                val dishItemDataResponse: DishItemData = dishItemUseCasesInterface.createDishDataObject(
                    createDishItemData = createDishItemData,
                )
                call.respond(HttpStatusCode.OK, dishItemDataResponse)
            }

            put {
                val dishItemData = call.receive<DishItemData>()
                dishItemUseCasesInterface.putDishDataObject(dishItemData = dishItemData)
                call.respond(HttpStatusCode.OK, "The dish was updated")
            }

            delete("{dish_id}") {
                val dishId: String? = call.parameters["dish_id"]

                if (dishId.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest, "Bad request")
                else {
                    dishItemUseCasesInterface.deleteDishItemObject(dishId = dishId)
                    call.respond(HttpStatusCode.OK, "The dish was deleted")
                }
            }

            get("list/{menu_id}") {
                val menuId: String? = call.parameters["menu_id"]

                if (menuId.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest, "Bad request")
                else {
                    val response = dishItemUseCasesInterface.getDishItemListByMenuId(
                        menuId = menuId,
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
            }

            get("list/{menu_id}/{section_id}") {
                val menuId: String? = call.parameters["menu_id"]
                val sectionId: String? = call.parameters["section_id"]

                if (menuId.isNullOrBlank() || sectionId.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Bad request")
                } else {
                    val response = dishItemUseCasesInterface.getDishItemListByMenuIdAndSectionId(
                        menuId = menuId,
                        sectionId = sectionId,
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
            }

            get("{dish_id}") {
                val dishId: String? = call.parameters["dish_id"]

                if (dishId.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest, "Bad request")
                else {
                    val response = dishItemUseCasesInterface.getDishItem(
                        dishId = dishId,
                    )
                    call.respond(HttpStatusCode.OK, response ?: "The dish was not found")
                }
            }
        }
    }

}