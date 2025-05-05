package com.example.routes

import com.example.models.DTO.response.ErrorResponse
import com.example.models.DTO.request.FlashcardAnswerDTO
import com.example.viewmodel.FlashcardAnswerService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.flashcardAnswerRoutes(flashcardAnswerService: FlashcardAnswerService) {
    post("/flashcards/answer") {
        try {
            val request = call.receive<FlashcardAnswerDTO>()
            val status = flashcardAnswerService.createFlashcardAnswer(request)
            call.respond(status)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = e.message ?: "Dados inválidos"))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(error = "Erro ao processar resposta", details = e.message)
            )
        }
    }
    
    get("/flashcard-answers") {
        try {
            val flashcardId = call.request.queryParameters["flashcardId"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = "ID do flashcard inválido"))
            
            val answers = flashcardAnswerService.getFlashcardAnswers(flashcardId)
            call.respond(answers)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(error = "Erro ao obter respostas", details = e.message)
            )
        }
    }
}