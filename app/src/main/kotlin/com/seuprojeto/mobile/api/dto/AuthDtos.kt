package com.seuprojeto.mobile.api.dto

import kotlinx.serialization.Serializable

/**
 * DTOs para autenticação
 */

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val message: String
)

@Serializable
data class LoginResponse(
    val message: String,
    val userId: Int?
)
