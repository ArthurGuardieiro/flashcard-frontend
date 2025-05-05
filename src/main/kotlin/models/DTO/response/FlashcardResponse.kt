package com.example.models.DTO.response

import kotlinx.serialization.Serializable

@Serializable
data class FlashcardResponse(
    val id: Int,
    val question: String,
    val answer: String,
    val type: String,
    val options: List<String> = emptyList(),
    val nextRepetition: String? = null,
    val repetitions: Int = 0,
    val easinessFactor: Float = 2.5f,
    val interval: Int = 1
) 