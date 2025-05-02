package com.example.DTO

import com.example.models.FlashcardType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class FlashcardDTO(
    val id: Int,
    val question: String,
    val answer: String,
    val type: FlashcardType,
    val options: List<String>? = null,
    val userId: Int,
    val locations: List<String>? = null,

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