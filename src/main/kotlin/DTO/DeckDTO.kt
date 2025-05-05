package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
data class DeckDTO(
    val name: String,
    val description: String,
    val userId: Int
)

@Serializable
data class DeckResponseDTO(
    val id: Int,
    val name: String,
    val description: String,
    val userId: Int,
    val flashcardCount: Int = 0
)
