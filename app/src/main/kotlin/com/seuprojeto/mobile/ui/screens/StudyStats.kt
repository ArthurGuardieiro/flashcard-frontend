package com.seuprojeto.mobile.ui.screens

import com.seuprojeto.mobile.model.FlashcardType
import org.threeten.bp.LocalDate

/**
 * Dados estatísticos sobre a atividade do usuário com flashcards.
 */
data class StudyStats(
    val totalReviews: Int = 0,
    val totalFlashcards: Int = 0,
    val totalStudyTime: Long = 0, // em minutos
    val streakDays: Int = 0,
    val reviewsLast7Days: Map<LocalDate, Int> = emptyMap(),
    val reviewsByType: Map<FlashcardType, Int> = emptyMap(),
    val reviewsByDifficulty: Map<String, Int> = mapOf(
        "Fácil" to 0,
        "Médio" to 0,
        "Difícil" to 0
    )
) 