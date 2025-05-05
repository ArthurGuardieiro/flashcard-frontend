package com.example

import com.example.di.appModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import org.slf4j.event.Level
import com.example.routes.configureRouting
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.module() {

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    configureHTTP()
    configureSerialization()
    configureMonitoring()
    configureRouting()
    // Banco de dados desativado, pois agora usamos o backend
    // configureDatabase()
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging) {
        level = Level.INFO
    }
}

// Função comentada pois agora usamos o banco de dados do backend
/*
fun Application.configureDatabase() {
    Database.connect("jdbc:sqlite:data.db", driver = "org.sqlite.JDBC")

    transaction {
        SchemaUtils.create(Flashcards)
        SchemaUtils.create(FlashcardAnswer)
        SchemaUtils.create(FlashcardLocationPriority)
        SchemaUtils.create(Users)
        SchemaUtils.create(Locations)
    }
}
*/
