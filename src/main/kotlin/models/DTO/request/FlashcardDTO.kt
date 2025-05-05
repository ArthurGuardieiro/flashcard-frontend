package com.example.models.DTO.request

import kotlinx.serialization.Serializable

@Serializable
data class FlashcardDTO(
    val id: Int? = null,
    val question: String,
    val answer: String,
    val type: String,
    val options: List<String>? = null,
    val userId: Int,
    val deckId: Int? = null,
    val nextRepetition: String = "",
    val repetitions: Int = 0,
    val easinessFactor: Float = 2.5f,
    val interval: Int = 1
) 