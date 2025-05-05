package com.example.DTO
import kotlinx.serialization.Serializable

@Serializable
data class FlashcardResponse(
    val id: Int,
    val question: String,
    val answer: String,
    val type: String,
    val deckId: Int? = null,
    val options: List<String>,
    val locations: List<String>,
    val isCorrect: Boolean? = null
)



