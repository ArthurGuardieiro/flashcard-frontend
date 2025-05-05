package com.example.models.DTO.response

import kotlinx.serialization.Serializable

@Serializable
data class FlashcardAnswerResponse(
    val id: Int? = null,
    val isCorrect: Boolean,
    val quality: Int,
    val correctAnswer: String? = null,
    val createdAt: String? = null
) 