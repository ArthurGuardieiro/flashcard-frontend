package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
data class FlashcardAnswerResponse(
    val flashcardId: Int,
    val userId: Int,
    val responseTimeMs: Long,
    val userAnswer:  String? = null,
    val location: String? = null, //será q fica na pergunta ou na resposta?
    val quality: Int,
    val isCorrect: Boolean
)
