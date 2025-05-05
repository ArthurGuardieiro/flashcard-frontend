package com.example.models.evaluation

/**
 * Avaliador de qualidade de respostas para flashcards.
 * Converte o tempo de resposta e acurácia em um valor de qualidade de 0-5 para o algoritmo SM-2.
 */
object AnswerQualityEvaluator {
    // Limites de tempo para considerar uma resposta rápida, média ou lenta (em ms)
    private const val FAST_RESPONSE_THRESHOLD = 3000L // 3 segundos
    private const val MEDIUM_RESPONSE_THRESHOLD = 10000L // 10 segundos

    /**
     * Avalia a qualidade da resposta com base no tempo e acurácia.
     * 
     * @param responseTimeMs tempo de resposta em milissegundos
     * @param isCorrect se a resposta está correta
     * @return um valor de qualidade de 0-5 conforme escala SM-2
     */
    fun evaluateQuality(responseTimeMs: Long, isCorrect: Boolean): Int {
        if (!isCorrect) {
            return if (responseTimeMs < FAST_RESPONSE_THRESHOLD) 1 else 0
        }
        
        return when {
            responseTimeMs < FAST_RESPONSE_THRESHOLD -> 5
            responseTimeMs < MEDIUM_RESPONSE_THRESHOLD -> 4
            else -> 3
        }
    }
} 