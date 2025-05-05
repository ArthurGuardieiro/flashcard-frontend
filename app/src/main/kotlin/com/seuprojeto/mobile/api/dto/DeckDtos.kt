package com.seuprojeto.mobile.api.dto

import kotlinx.serialization.Serializable

/**
 * DTOs para decks
 */

@Serializable
data class DeckDTO(
    val name: String,
    val description: String,
    val isPublic: Boolean
)

@Serializable
data class DeckResponse(
    val id: Int,
    val name: String,
    val description: String,
    val userId: Int,
    val isPublic: Boolean
)
