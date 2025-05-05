package com.example.models.algorithm

import com.example.viewmodel.LocationService
import java.time.LocalDateTime

/**
 * Scheduler que considera a localização para priorizar a revisão de flashcards.
 * Integra o algoritmo de repetição espaçada com a conscientização de contexto baseada em localização.
 */
class LocationAwareScheduler(private val locationService: LocationService) {

    /**
     * Factor que determina quanto a localização afeta a prioridade dos flashcards.
     * Valores maiores dão mais peso à localização atual.
     */
    private val LOCATION_WEIGHT_FACTOR = 2.0

    /**
     * Calcula a prioridade de revisão do flashcard baseado na localização atual e no algoritmo SM-2.
     * 
     * @param flashcardId ID do flashcard
     * @param userId ID do usuário
     * @param locationId ID da localização atual (opcional, usa localização padrão se não informado)
     * @param nextRepetitionDate data da próxima repetição calculada pelo SM-2
     * @param locationPriority prioridade associada a esse flashcard nesta localização específica
     * @return valor de prioridade (menor = mais prioritário)
     */
    fun calculateReviewPriority(
        flashcardId: Int,
        userId: Int,
        locationId: Int? = null,
        nextRepetitionDate: LocalDateTime,
        locationPriority: Int = 1
    ): Double {
        // Obtém a localização atual ou usa a padrão se não informada
        val currentLocationId = locationId ?: locationService.getDefaultLocation(userId)
        
        // Calcula a prioridade temporal (quanto mais atrasado, mais prioritário - valor negativo)
        val daysUntilDue = calculateDaysUntilDue(nextRepetitionDate)
        
        // Prioridade baseada na localização (valores menores = mais prioritário)
        val locationFactor = if (locationPriority > 0) 1.0 / locationPriority else 1.0
        
        // Combina os fatores: tempo e localização
        // Se o cartão estiver atrasado (daysUntilDue < 0), a localização tem ainda mais peso
        val isOverdue = daysUntilDue < 0
        val locationBoost = if (isOverdue) LOCATION_WEIGHT_FACTOR * 1.5 else LOCATION_WEIGHT_FACTOR
        
        // Fórmula de prioridade: menor valor = mais prioritário
        // Cartões atrasados recebem prioridade extra (valor negativo)
        return daysUntilDue - (locationFactor * locationBoost)
    }
    
    /**
     * Calcula quantos dias faltam até a revisão agendada.
     * Retorna valor negativo se estiver atrasado.
     */
    private fun calculateDaysUntilDue(nextRepetitionDate: LocalDateTime): Double {
        val now = LocalDateTime.now()
        val daysBetween = java.time.Duration.between(now, nextRepetitionDate).toDays().toDouble()
        return daysBetween
    }
    
    /**
     * Calcula a nova data de repetição considerando algoritmo SM-2 e ajuste de localização.
     */
    fun calculateNextRepetition(
        repetitions: Int,
        easinessFactor: Float,
        interval: Int,
        quality: Int
    ): Triple<Int, Float, Int> {
        return SpacedRepetition.calculateNewParameters(repetitions, easinessFactor, interval, quality)
    }
} 