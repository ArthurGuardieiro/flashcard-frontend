package com.seuprojeto.mobile.api.dto

import com.seuprojeto.mobile.model.FlashcardType
import kotlinx.serialization.Serializable

/**
 * DTOs para flashcards
 */

@Serializable
data class CreateFlashcardDTO(
    val question: String,
    val answer: String,
    val type: FlashcardType,
    val options: List<String>? = null,
    val userId: Int,
    val locationId: Int,
    val deckId: Int
)

@Serializable
data class FlashcardResponse(
    val id: Int,
    val question: String,
    val answer: String,
    val type: String,
    val options: List<String>,
    val nextRepetition: String,
    val repetitions: Int,
    val easinessFactor: Float,
    val interval: Int
)

@Serializable
data class ReviewDTO(
    val quality: Int, // 0-5 onde 0 é mais difícil e 5 é mais fácil
    val locationId: Int
)

@Serializable
data class FlashcardAnswerDTO(
    val flashcardId: Int,
    val userId: Int,
    val isCorrect: Boolean,
    val locationId: Int
)

@Serializable
data class FlashcardAnswerResponse(
    val id: Int,
    val flashcardId: Int,
    val userId: Int,
    val isCorrect: Boolean,
    val createdAt: String
)
