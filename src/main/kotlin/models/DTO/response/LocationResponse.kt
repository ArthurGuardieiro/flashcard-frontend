package com.example.models.DTO.response

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val id: Int,
    val name: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isDefault: Boolean = false
) 