package com.example.models.algorithm

import java.time.LocalDateTime
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Implementação do algoritmo SuperMemo-2 para repetição espaçada.
 * Referência: https://www.supermemo.com/en/archives1990-2015/english/ol/sm2
 */
object SpacedRepetition {
    // Mínimo valor permitido para o fator de facilidade
    private const val MIN_EASINESS_FACTOR = 1.3f

    /**
     * Calcula os novos parâmetros de repetição espaçada com base na qualidade da resposta.
     *
     * @param repetitions número atual de repetições consecutivas bem-sucedidas
     * @param easinessFactor atual fator de facilidade
     * @param interval atual intervalo em dias
     * @param quality qualidade da resposta (0-5), onde 0 é a pior e 5 é a melhor
     * @return Triple contendo os novos valores: (repetições, fator de facilidade, intervalo)
     */
    fun calculateNewParameters(
        repetitions: Int,
        easinessFactor: Float,
        interval: Int,
        quality: Int
    ): Triple<Int, Float, Int> {
        // Ajuste de qualidade para range válido
        val validQuality = quality.coerceIn(0, 5)

        // Cálculo do novo fator de facilidade
        val newEasinessFactor = calculateNewEasinessFactor(easinessFactor, validQuality)

        // Se a resposta for muito ruim (0-2), recomeça o processo de aprendizagem
        if (validQuality < 3) {
            return Triple(0, newEasinessFactor, 1)
        }

        // Cálculo das repetições e intervalo para respostas boas (3-5)
        val newRepetitions = repetitions + 1
        val newInterval = calculateNewInterval(newRepetitions, interval, newEasinessFactor)

        return Triple(newRepetitions, newEasinessFactor, newInterval)
    }

    /**
     * Calcula o novo fator de facilidade baseado na qualidade da resposta.
     */
    private fun calculateNewEasinessFactor(oldEasinessFactor: Float, quality: Int): Float {
        val newEF = oldEasinessFactor + (0.1f - (5 - quality) * (0.08f + (5 - quality) * 0.02f))
        return newEF.coerceAtLeast(MIN_EASINESS_FACTOR)
    }

    /**
     * Calcula o novo intervalo com base no número de repetições.
     */
    private fun calculateNewInterval(repetitions: Int, oldInterval: Int, easinessFactor: Float): Int {
        return when {
            repetitions == 1 -> 1
            repetitions == 2 -> 6
            else -> (oldInterval * easinessFactor).roundToInt()
        }
    }

    /**
     * Calcula a próxima data de repetição.
     */
    fun calculateNextRepetitionDate(interval: Int): LocalDateTime {
        return LocalDateTime.now().plusDays(interval.toLong())
    }
} 