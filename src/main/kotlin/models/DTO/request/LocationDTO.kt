package com.example.models.DTO.request

import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
    val id: Int? = null,
    val name: String,
    val userId: Int,
    val latitude: Double? = null,
    val longitude: Double? = null
) 