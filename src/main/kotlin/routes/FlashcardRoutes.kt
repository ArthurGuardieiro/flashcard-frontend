package com.example.routes

import com.example.models.DTO.response.ErrorResponse
import com.example.models.DTO.request.FlashcardDTO
import com.example.models.DTO.request.ReviewDTO
import com.example.viewmodel.FlashcardService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.flashcardRoutes(flashcardService: FlashcardService) {
    get("/flashcards") {
        val userId = call.request.queryParameters["userId"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Missing userId"))

        val flashcards = flashcardService.getFlashcards(userId)
        call.respond(flashcards)
    }

    post("/flashcards") {
        try {
            val request = call.receive<FlashcardDTO>()
            val status = flashcardService.createFlashcard(request)
            call.respond(status)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = e.message ?: "Invalid data"))
        }
    }

    put("/flashcards/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@put call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Missing or invalid ID"))

        try {
            val request = call.receive<FlashcardDTO>()
            val status = flashcardService.updateFlashcard(id, request)
            call.respond(status)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = e.message ?: "Invalid data"))
        }
    }
    
    post("/flashcards/{id}/review") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Missing or invalid ID"))
            
        val userId = call.request.queryParameters["userId"]?.toIntOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Missing userId"))
            
        try {
            val review = call.receive<ReviewDTO>()
            val status = flashcardService.reviewFlashcard(id, userId, review)
            call.respond(status)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = e.message ?: "Invalid data"))
        }
    }
}