package com.seuprojeto.mobile.api.dto

import kotlinx.serialization.Serializable

/**
 * DTOs para localizações
 */

@Serializable
data class LocationDTO(
    val name: String,
    val userId: Int
)

@Serializable
data class LocationResponse(
    val id: Int,
    val name: String,
    val userId: Int
)
