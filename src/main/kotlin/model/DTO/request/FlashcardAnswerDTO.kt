package com.example.models.DTO.request

import kotlinx.serialization.Serializable

@Serializable
data class FlashcardAnswerDTO(
    val id: Int,
    val flashcardId: Int,
    val userId: Int,
    val responseTimeMs: Long,
    val userAnswer:  String? = null,
    val location: String? = null, //será q fica na pergunta ou na resposta?
    val quality: Int,
    val isCorrect: Boolean
){
    init {
        require(responseTimeMs > 0) { "Tempo de resposta inválido" }
        require(flashcardId > 0 && userId > 0) { "IDs devem ser positivos" }
    }
}
