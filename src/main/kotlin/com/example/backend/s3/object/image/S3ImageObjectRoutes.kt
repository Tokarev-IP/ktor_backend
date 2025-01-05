package com.example.backend.s3.`object`.image

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray

fun Application.configureS3ImageObjectRoutes(
    s3ObjectUseCases: S3ImageObjectUseCases,
) {
    routing {
        route("/bucket/object/image") {
            post("/{image_id}") {
                val fileName = call.parameters["image_id"]
                val multipartData = call.receiveMultipart()

                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val contentType = part.contentType?.contentType
                            val fileBytes = part.provider().readRemaining().readByteArray()
                            if (fileName != null) {
                                contentType?.let { type ->
                                    s3ObjectUseCases.putObjectIntoBucket(
                                        objectName = fileName,
                                        objectBytes = fileBytes,
                                        objectType = type,
                                    )
                                    call.respond(HttpStatusCode.OK, "File was uploaded")
                                } ?: call.respond(
                                    HttpStatusCode.UnsupportedMediaType,
                                    "Type of file is unsupported"
                                )
                            } else
                                call.respond(HttpStatusCode.BadRequest, "Incorrect Dish ID")
                        }

                        else -> {
                            call.respond(HttpStatusCode.BadRequest, "File has not been attached")
                        }
                    }

                    part.dispose()
                }
            }

            get("/{image_id}") {
                val imageId = call.parameters["image_id"]
                if (imageId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Parameters are null")
                } else {
                    val objectByteArray = s3ObjectUseCases.getObjectFromBucket(
                        objectName = imageId,
                    )
                    objectByteArray?.let {
                        call.respond(HttpStatusCode.OK, objectByteArray.toTypedArray())
                    } ?: call.respond(HttpStatusCode.NotFound, "The file was not found")
                }
            }

            delete("/{image_id}") {
                val fileName = call.parameters["file_name"]
                if (fileName == null) {
                    call.respond(HttpStatusCode.BadRequest, "Parameters are null")
                } else {
                    s3ObjectUseCases.deleteObjectFromBucket(
                        objectName = fileName,
                    )
                    call.respond(HttpStatusCode.OK, "The file was deleted")
                }
            }

            get("/check/{image_id}") {
                val imageId = call.parameters["image_id"]
                if (imageId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Parameters are null")
                } else {
                    val isExist = s3ObjectUseCases.isObjectExistedAtBucket(
                        objectName = imageId,
                    )
                    call.respond(HttpStatusCode.OK, isExist)
                }
            }
        }
    }
}