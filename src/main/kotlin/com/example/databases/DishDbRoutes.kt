package com.example.databases

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDishDbRoutes() {
    val dishService = DishService(DatabaseObject.database)
    routing {
        // Create dish
        post("/dish") {
            val dish = call.receive<ExposedDish>()
            val id = dishService.create(dish)
            call.respond(HttpStatusCode.Created, id)
        }

        // Read dish
        get("/dish/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            val dish = dishService.read(id)
            if (dish != null) {
                call.respond(HttpStatusCode.OK, dish)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        //Read list of all  dishes
        get("/dishes") {
            val list = dishService.readAll()
            if (list.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "Empty dishes")
            } else {
                call.respond(list)
            }
        }

        // Update dish
        put("/dish/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            val dish = call.receive<ExposedDish>()
            dishService.update(id, dish)
            call.respond(HttpStatusCode.OK)
        }

        // Delete dish
        delete("/dish/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            dishService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}

object DatabaseObject {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
}