package com.example.routes

import com.example.models.DTO.request.AuthResponse
import com.example.models.DTO.request.RegisterRequest
import com.example.models.DTO.response.LoginResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import models.Users
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.authRoutes() {
    post("/register") {
        try {
            val request = call.receive<RegisterRequest>().apply {
                require(username.isNotBlank()) { "Username cannot be blank" }
                require(password.length >= 6) { "Password must be at least 6 characters" }
            }

            val passwordHash = request.password.hashCode().toString()

            val exists = transaction {
                models.Users.select { models.Users.username eq request.username }.any()
            }

            if (exists) {
                call.respond(io.ktor.http.HttpStatusCode.Conflict, AuthResponse("Usuário já existe"))
                return@post
            }

            transaction {
                models.Users.insert {
                    it[models.Users.username] = request.username
                    it[models.Users.passwordHash] = passwordHash
                }
            }

            call.respond(io.ktor.http.HttpStatusCode.Created, AuthResponse("Usuário registrado com sucesso"))
        } catch (e: IllegalArgumentException) {
            call.respond(io.ktor.http.HttpStatusCode.BadRequest, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(
                io.ktor.http.HttpStatusCode.InternalServerError,
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
                models.Users.select {
                    models.Users.username eq request.username and (models.Users.passwordHash eq passwordHash)
                }.singleOrNull()
            }

            if (user != null) {
                call.respond(LoginResponse("Login bem-sucedido", user[models.Users.id]))
            } else {
                call.respond(io.ktor.http.HttpStatusCode.Unauthorized, AuthResponse("Credenciais inválidas"))
            }
        } catch (e: IllegalArgumentException) {
            call.respond(io.ktor.http.HttpStatusCode.BadRequest, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(
                io.ktor.http.HttpStatusCode.InternalServerError,
                mapOf("error" to "Login failed: ${e.message}")
            )
        }
    }
}