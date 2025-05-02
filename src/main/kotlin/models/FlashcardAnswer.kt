package com.example.models

import models.Flashcards
import org.jetbrains.exposed.dao.id.IntIdTable

object FlashcardAnswer : IntIdTable() {
    val flashcardId = integer("flashcard_id").references(Flashcards.id)
    val userId = integer("user_id")
    val responseTimeMs = long("response_time_ms")
    val userAnswer = text("user_answer").nullable()
    val quality = integer("quality")
    val isCorrect = bool("is_correct")
}