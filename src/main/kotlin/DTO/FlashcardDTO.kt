package com.example.DTO

import com.example.models.FlashcardType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class FlashcardDTO(
    val question: String,
    val answer: String,
    val userId: Int,
    val type: FlashcardType,
    val options: List<String>? = null,
    val locations: List<String>? =null,
    val quality: Int? = null,
    @Contextual
    val nextRepetition: LocalDateTime = LocalDateTime.now(),
    val repetitions: Int = 0,
    val easinessFactor: Float = 2.5.toFloat(),
    val interval: Int = 1
){
    fun withUpdatedRepetitionProperties(
        newRepetitions: Int,
        newEasinessFactor: Float,
        newNextRepetitionDate: LocalDateTime,
        newInterval: Int
    ) = copy(repetitions = newRepetitions,
        easinessFactor = newEasinessFactor,
        nextRepetition = newNextRepetitionDate,
        interval = newInterval
    )
}