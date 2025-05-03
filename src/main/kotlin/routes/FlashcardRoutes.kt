package com.example.routes

import com.example.models.DTO.request.FlashcardDTO
import com.example.models.DTO.response.FlashcardResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import models.Flashcards
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

fun Route.flashcardRoutes() {
    get("/flashcards") {
        val userId = call.request.queryParameters["userId"]?.toIntOrNull()
            ?: throw IllegalArgumentException("Missing userId")

        val flashcards = transaction {
            Flashcards.select { Flashcards.userId eq userId }
                .map { it.toFlashcardResponse() }
        }
        call.respond(flashcards)
    }

    get("/flashcards") {
        val userId = call.request.queryParameters["userId"]?.toIntOrNull()
        if (userId == null) {
            call.respond(io.ktor.http.HttpStatusCode.BadRequest, "Missing userId")
            return@get
        }

        try {
            val flashcards = transaction {
                models.Flashcards.select { models.Flashcards.userId eq userId }
                    .map {
                        FlashcardResponse(
                            id = it[models.Flashcards.id].value,
                            question = it[models.Flashcards.question],
                            answer = it[models.Flashcards.answer],
                            type = it[models.Flashcards.type].toString(),
                            options = it[models.Flashcards.options]?.split(";") ?: emptyList(),
                            nextRepetition = java.time.LocalDateTime.parse(it[models.Flashcards.nextRepetition], dateTimeFormatter)
                                .toString(),
                            repetitions = it[models.Flashcards.repetitions],
                            easinessFactor = it[models.Flashcards.easinessFactor],
                            interval = it[models.Flashcards.interval]
                        )
                    }
            }
            call.respond(flashcards)
        } catch (e: Exception) {
            call.respond(
                io.ktor.http.HttpStatusCode.InternalServerError,
                "Error retrieving flashcards: ${e.message}"
            )
        }
    }

    post("/flashcards") {
        val request = call.receive<FlashcardDTO>().validate()

        transaction {
            Flashcards.insert {
                it.fromDTO(request)
            }
        }
        call.respond(HttpStatusCode.Created)
    }

    put("/flashcards/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw IllegalArgumentException("Missing or invalid ID")

        val request = call.receive<FlashcardDTO>().validate()

        transaction {
            Flashcards.update({ Flashcards.id eq id }) {
                it.fromDTO(request)
            }
        }
        call.respond(HttpStatusCode.OK)
    }
}

// Extensions para melhor organização
private fun ResultRow.toFlashcardResponse(): FlashcardResponse {
    return FlashcardResponse(
        id = this[Flashcards.id].value,
        question = this[Flashcards.question],
        answer = this[Flashcards.answer],
        type = this[Flashcards.type].toString(),
        options = this[Flashcards.options]?.split(";") ?: emptyList(),
        nextRepetition = this[Flashcards.nextRepetition],
        repetitions = this[Flashcards.repetitions],
        easinessFactor = this[Flashcards.easinessFactor],
        interval = this[Flashcards.interval]
    )
}

private fun FlashcardDTO.validate(): FlashcardDTO {
    require(question.isNotBlank()) { "Question cannot be blank" }
    require(answer.isNotBlank()) { "Answer cannot be blank" }
    require(userId > 0) { "Invalid user ID" }
    require(repetitions >= 0) { "Repetitions must be >= 0" }
    require(easinessFactor >= 1.3f) { "Easiness factor must be >= 1.3" }
    require(interval >= 1) { "Interval must be >= 1" }
    return this
}
