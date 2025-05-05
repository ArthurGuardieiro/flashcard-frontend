package com.seuprojeto.mobile.model

import org.threeten.bp.LocalDateTime
import java.util.UUID

/**
 * Classe base para todos os tipos de flashcards.
 * 
 * @property id Identificador único do flashcard
 * @property title Título do flashcard
 * @property createdAt Data de criação do flashcard
 * @property lastReviewed Data da última revisão
 * @property reviewCount Número de revisões
 * @property locationId Identificador da localização onde o flashcard foi revisado por último
 * @property type Tipo do flashcard
 */
data class Flashcard(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var lastReviewed: LocalDateTime? = null,
    var reviewCount: Int = 0,
    var locationId: String? = null,
    val type: FlashcardType
) {
    // Campos específicos para cada tipo de flashcard
    
    // Tipo BASIC (frente e verso)
    var front: String? = null
    var back: String? = null
    
    // Tipo CLOZE (omissão de palavras)
    var fullText: String? = null
    var hiddenWords: List<String>? = null
    
    // Tipo TYPING (digitar a resposta)
    var question: String? = null
    var correctAnswer: String? = null
    
    // Tipo MULTIPLE_CHOICE (múltipla escolha)
    var multipleChoiceQuestion: String? = null
    var options: List<String>? = null
    var correctOptionIndex: Int? = null
    
    /**
     * Calcula o intervalo para a próxima revisão baseado no algoritmo de repetição espaçada.
     * Implementação provisória, será substituída pela lógica do backend.
     */
    fun calculateNextReviewDate(): LocalDateTime {
        // Lógica de repetição espaçada simplificada
        val multiplier = when (reviewCount) {
            0 -> 1
            1 -> 3
            2 -> 7
            3 -> 14
            4 -> 30
            else -> 60
        }
        
        return LocalDateTime.now().plusDays(multiplier.toLong())
    }
} 