package com.example

import com.example.DTO.FlashcardDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Flashcards
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import DTO.RegisterRequest
import DTO.AuthResponse
import com.example.DTO.FlashcardResponse
import com.example.DTO.LoginResponse
import models.Users
import org.jetbrains.exposed.sql.*
import com.example.models.FlashcardType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun Application.configureRouting() {
    routing {
        val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        get("/flashcards") {
            val userId = call.request.queryParameters["userId"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing userId")
                return@get
            }

            try {
                val flashcards = transaction {
                    Flashcards.select { Flashcards.userId eq userId }
                        .map {
                            FlashcardResponse(
                                id = it[Flashcards.id].value,
                                question = it[Flashcards.question],
                                answer = it[Flashcards.answer],
                                type = it[Flashcards.type].toString(),
                                options = it[Flashcards.options]?.split(";") ?: emptyList(),
                                locations = it[Flashcards.locations]?.split(";") ?: emptyList(),
                                quality = it[Flashcards.quality],
                                nextRepetition = LocalDateTime.parse(it[Flashcards.nextRepetition], dateTimeFormatter)
                                    .toString(),
                                repetitions = it[Flashcards.repetitions],
                                easinessFactor = it[Flashcards.easinessFactor],
                                interval = it[Flashcards.interval]
                            )
                        }
                }
                call.respond(flashcards)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Error retrieving flashcards: ${e.message}"
                )
            }
        }

        post("/flashcards") {
            try {
                val request = call.receive<FlashcardDTO>().apply {
                    require(question.isNotBlank()) { "Question cannot be blank" }
                    require(answer.isNotBlank()) { "Answer cannot be blank" }
                    require(userId > 0) { "Invalid user ID" }
                    require(repetitions >= 0) { "Repetitions must be >= 0" }
                    require(easinessFactor >= 1.3f) { "Easiness factor must be >= 1.3" }
                    require(interval >= 1) { "Interval must be >= 1" }
                }

                transaction {
                    Flashcards.insert {
                        it[question] = request.question
                        it[answer] = request.answer
                        it[userId] = request.userId
                        it[type] = request.type
                        it[options] = request.options?.joinToString(";")
                        val lLocations = request.locations?.joinToString(";")
                        if (!lLocations.isNullOrBlank() && lLocations.length > 6) {
                            it[locations] = lLocations
                        }
                        it[quality] = request.quality
                        it[nextRepetition] = request.nextRepetition.format(dateTimeFormatter)
                        it[repetitions] = request.repetitions
                        it[easinessFactor] = request.easinessFactor
                        it[interval] = request.interval
                    }
                }
                call.respond(HttpStatusCode.Created)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to create flashcard: ${e.message}")
                )
            }
        }

        post("/register") {
            try {
                val request = call.receive<RegisterRequest>().apply {
                    require(username.isNotBlank()) { "Username cannot be blank" }
                    require(password.length >= 6) { "Password must be at least 6 characters" }
                }

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
                        it[Users.passwordHash] = passwordHash
                    }
                }

                call.respond(HttpStatusCode.Created, AuthResponse("Usuário registrado com sucesso"))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Registration failed: ${e.message}")
                )
            }
        }

        post("/login") {
            try {
                val request = call.receive<RegisterRequest>().apply {
                    require(username.isNotBlank()) { "Username cannot be blank" }
                    require(password.isNotBlank()) { "Password cannot be blank" }
                }

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
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Login failed: ${e.message}")
                )
            }
        }

        put("/flashcards/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: throw IllegalArgumentException("Missing or invalid ID")

                val request = call.receive<FlashcardDTO>().apply {
                    require(question.isNotBlank()) { "Question cannot be blank" }
                    require(answer.isNotBlank()) { "Answer cannot be blank" }
                }

                transaction {
                    Flashcards.update({ Flashcards.id eq id }) {
                        it[question] = request.question
                        it[answer] = request.answer
                        it[userId] = request.userId
                        it[type] = request.type
                        it[options] = request.options?.joinToString(";")
                        val lLocations = request.locations?.joinToString(";")
                        if (!lLocations.isNullOrBlank() && lLocations.length > 6) {
                            it[locations] = lLocations
                        }
                        it[quality] = request.quality
                        it[nextRepetition] = request.nextRepetition.format(dateTimeFormatter)
                        it[repetitions] = request.repetitions
                        it[easinessFactor] = request.easinessFactor
                        it[interval] = request.interval
                    }
                }

                call.respond(HttpStatusCode.OK)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to update flashcard: ${e.message}")
                )
            }
        }
    }
}