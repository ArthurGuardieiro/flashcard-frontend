package com.seuprojeto.mobile.model

import java.util.UUID

/**
 * Representa um usuário do aplicativo.
 */
data class User(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val email: String? = null,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)
