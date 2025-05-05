package com.seuprojeto.mobile.api.dto

import kotlinx.serialization.Serializable

/**
 * DTO para respostas de erro
 */

@Serializable
data class ErrorResponse(
    val error: String,
    val details: String? = null
)
