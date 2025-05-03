package com.example.routes

import com.example.model.DTO.response.ErrorResponse
import com.example.models.DTO.request.FlashcardAnswerDTO
import com.example.viewmodel.FlashcardAnswerService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.flashcardAnswerRoutes(flashcardAnswerService: FlashcardAnswerService) {
    post("/flashcards/answer") {
        try {
            val request = call.receive<FlashcardAnswerDTO>()
            val (response, status) = flashcardAnswerService.processAnswer(request)
            call.respond(status, response)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = e.message ?: "Dados inválidos"))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(error = "Erro ao processar resposta", details = e.message)
            )
        }
    }
}