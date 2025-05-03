package com.example.routes

import com.example.viewmodel.AuthService
import com.example.viewmodel.FlashcardAnswerService
import com.example.viewmodel.FlashcardService
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val authService by inject<AuthService>()
    val flashcardService by inject<FlashcardService>()
    val flashcardAnswerService by inject<FlashcardAnswerService>()

    routing {
        route("/api") {
            authRoutes(authService)
            flashcardRoutes(flashcardService)
            flashcardAnswerRoutes(flashcardAnswerService)
        }
    }
}