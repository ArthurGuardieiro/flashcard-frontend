package com.example.models.DTO.request

import kotlinx.serialization.Serializable

@Serializable
data class FlashcardAnswerDTO(
    val flashcardId: Int,
    val userId: Int,
    val responseTimeMs: Long,
    val userAnswer: String? = null,
    val locationId: Int? = null,
    val quality: Int? = null,
    val isCorrect: Boolean? = null,
    val createdAt: String? = null
) 