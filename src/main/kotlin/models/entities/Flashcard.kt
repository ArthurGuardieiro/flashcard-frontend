package com.example.models.entities

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Flashcards : IntIdTable() {
    val question = text("question")
    val answer = text("answer")
    val type = varchar("type", 20)
    val options = text("options").nullable()
    val userId = integer("user_id").references(Users.id)
    val deckId = integer("deck_id").references(Deck.id)
    val nextRepetition = varchar("next_repetition", 64).default(CurrentDateTime.toString())
    val repetitions = integer("repetitions").default(0)
    val easinessFactor = float("easiness_factor").default(2.5f)
    val interval = integer("interval").default(1)
} 