package com.seuprojeto.mobile.model

import java.util.UUID
import org.threeten.bp.LocalDateTime

/**
 * Representa um baralho de flashcards.
 * 
 * @property id Identificador único do baralho
 * @property name Nome do baralho
 * @property description Descrição do baralho
 * @property createdAt Data de criação do baralho
 * @property lastStudied Data do último estudo
 * @property cardCount Número de flashcards no baralho
 * @property coverColor Cor de capa do baralho (para UI)
 */
data class Deck(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var lastStudied: LocalDateTime? = null,
    var cardCount: Int = 0,
    val coverColor: Long = 0xFF2196F3 // Azul padrão
)
