package com.example

import com.example.DTO.FlashcardDTO
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Flashcards
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.*
import DTO.RegisterRequest
import DTO.AuthResponse
import com.example.DTO.DeckDTO
import com.example.DTO.DeckResponseDTO
import com.example.DTO.FlashcardResponse
import com.example.DTO.FlashcardResponseDTO
import com.example.DTO.LoginResponse
import models.Decks
import models.Users
import org.jetbrains.exposed.sql.*
import com.example.models.FlashcardType
import org.jetbrains.exposed.dao.id.EntityID


fun Application.configureRouting() {
    routing {

        // Deck management endpoints
        get("/decks") {
            val userId = call.request.queryParameters["userId"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing userId")
                return@get
            }

            val decks = transaction {
                (Decks.select { Decks.userId eq userId })
                    .map { deckRow ->
                        val deckId = deckRow[Decks.id].value
                        val flashcardCount = Flashcards.select { Flashcards.deckId eq deckId }.count()
                        DeckResponseDTO(
                            id = deckId,
                            name = deckRow[Decks.name],
                            description = deckRow[Decks.description],
                            userId = deckRow[Decks.userId],
                            flashcardCount = flashcardCount.toInt()
                        )
                    }
            }

            call.respond(decks)
        }

        get("/decks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid deck ID")
                return@get
            }

            val deck = transaction {
                Decks.select { Decks.id eq id }.singleOrNull()?.let { deckRow ->
                    val flashcardCount = Flashcards.select { Flashcards.deckId eq id }.count()
                    DeckResponseDTO(
                        id = deckRow[Decks.id].value,
                        name = deckRow[Decks.name],
                        description = deckRow[Decks.description],
                        userId = deckRow[Decks.userId],
                        flashcardCount = flashcardCount.toInt()
                    )
                }
            }

            if (deck != null) {
                call.respond(deck)
            } else {
                call.respond(HttpStatusCode.NotFound, "Deck not found")
            }
        }

        post("/decks") {
            val request = call.receive<DeckDTO>()
            val deckId = transaction {
                Decks.insert {
                    it[name] = request.name
                    it[description] = request.description
                    it[userId] = request.userId
                }.resultedValues?.firstOrNull()?.get(Decks.id)
            }

            if (deckId != null) {
                call.respond(HttpStatusCode.Created, mapOf("id" to deckId.value))
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Failed to create deck")
            }
        }

        put("/decks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid deck ID")
                return@put
            }

            val request = call.receive<DeckDTO>()
            val updated = transaction {
                Decks.update({ Decks.id eq id }) {
                    it[name] = request.name
                    it[description] = request.description
                }
            }

            if (updated > 0) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Deck not found")
            }
        }

        delete("/decks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid deck ID")
                return@delete
            }

            transaction {
                // First, set deckId to null for all flashcards in this deck
                Flashcards.update({ Flashcards.deckId eq id }) {
                    it[deckId] = null
                }
                
                // Then delete the deck
                Decks.deleteWhere { Decks.id eq id }
            }

            call.respond(HttpStatusCode.OK)
        }

        // Get flashcards by deck ID
        get("/decks/{deckId}/flashcards") {
            val deckId = call.parameters["deckId"]?.toIntOrNull()
            if (deckId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid deck ID")
                return@get
            }

            val flashcards = transaction {
                Flashcards.select { Flashcards.deckId eq deckId }
                    .map {
                        FlashcardResponse(
                            id = it[Flashcards.id].value,
                            question = it[Flashcards.question],
                            answer = it[Flashcards.answer],
                            type = it[Flashcards.type].toString(),
                            deckId = it[Flashcards.deckId],
                            options = it[Flashcards.options]?.split(";") ?: emptyList(),
                            locations = it[Flashcards.locations]?.split(";") ?: emptyList(),
                            isCorrect = it[Flashcards.isCorrect]
                        )
                    }
            }

            call.respond(flashcards)
        }

        // Original flashcards endpoint, now with optional deckId filter
        get("/flashcards") {
            val userId = call.request.queryParameters["userId"]?.toIntOrNull()
            val deckId = call.request.queryParameters["deckId"]?.toIntOrNull()
            
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing userId")
                return@get
            }

            val flashcards = transaction {
                val query = if (deckId != null) {
                    Flashcards.select { (Flashcards.userId eq userId) and (Flashcards.deckId eq deckId) }
                } else {
                    Flashcards.select { Flashcards.userId eq userId }
                }
                
                query.map {
                    FlashcardResponse(
                        id = it[Flashcards.id].value,
                        question = it[Flashcards.question],
                        answer = it[Flashcards.answer],
                        type = it[Flashcards.type].toString(),
                        deckId = it[Flashcards.deckId],
                        options = it[Flashcards.options]?.split(";") ?: emptyList(),
                        locations = it[Flashcards.locations]?.split(";") ?: emptyList(),
                        isCorrect = it[Flashcards.isCorrect]
                    )
                }
            }

            call.respond(flashcards)
        }



        post("/flashcards") {
            val request = call.receive<FlashcardDTO>()
            transaction {
                Flashcards.insert {
                    it[question] = request.question
                    it[answer] = request.answer
                    it[userId] = request.userId
                    it[type] = request.type
                    it[options] = request.options?.joinToString(";")
                    val lLocations = request.locations?.joinToString(";")
                    if(!lLocations.isNullOrBlank() && lLocations.length > 6){
                        it[locations] = lLocations
                    }
                    it[isCorrect] = request.isCorrect
                }
            }
            call.respond(HttpStatusCode.Created)
        }


        post("/register") {
            val request = call.receive<RegisterRequest>()

            val passwordHash = request.password.hashCode().toString()

            val exists = transaction {
                Users.select { Users.username eq request.username }.any()
            }

            if (exists) {
                call.respond(HttpStatusCode.Conflict, AuthResponse("Usuário já existe"))
                return@post
            }

            transaction {
                Users.insert {
                    it[Users.username] = request.username
                    it[Users.passwordHash] = request.password.hashCode().toString()
                }
            }

            call.respond(HttpStatusCode.Created, AuthResponse("Usuário registrado com sucesso"))
        }

        post("/login") {
            val request = call.receive<RegisterRequest>()
            val passwordHash = request.password.hashCode().toString()

            val user = transaction {
                Users.select {
                    Users.username eq request.username and (Users.passwordHash eq passwordHash)
                }.singleOrNull()
            }

            if (user != null) {
                call.respond(LoginResponse("Login bem-sucedido", user[Users.id]))
            } else {
                call.respond(HttpStatusCode.Unauthorized, AuthResponse("Credenciais inválidas"))
            }

        }

        put("/flashcards/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid ID")
                return@put
            }

            val request = call.receive<FlashcardDTO>()

            transaction {
                Flashcards.update({ Flashcards.id eq id }) {
                    it[question] = request.question
                    it[answer] = request.answer
                    it[userId] = request.userId
                    it[type] = FlashcardType.valueOf(request.type.name)
                    it[options] = request.options?.joinToString(";")
                    val lLocations = request.locations?.joinToString(";")
                    if(!lLocations.isNullOrBlank() && lLocations.length > 6){
                        it[locations] = lLocations
                    }
                    it[isCorrect] = request.isCorrect
                }
            }

            call.respond(HttpStatusCode.OK)
        }


    }
}
