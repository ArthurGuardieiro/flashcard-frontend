package models

import com.example.models.FlashcardType
import org.jetbrains.exposed.dao.id.IntIdTable

object Flashcards : IntIdTable() {
    val question = varchar("question", 255)
    val answer = varchar("answer", 255)
    val userId = integer("user_id")
    val type = enumerationByName("type", 50, FlashcardType::class)
    val options = text("options").nullable()
    val locations = text("locations").nullable()
    val quality = integer("quality").nullable()
    val nextRepetition = text("next_repetition")
    val repetitions = integer("repetitions")
    val easinessFactor = float("easiness_factor")
    val interval = integer("interval")
}