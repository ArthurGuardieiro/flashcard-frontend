package com.example.models.entities

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object FlashcardLocationPriority : IntIdTable() {
    val flashcardId = integer("flashcard_id").references(Flashcards.id)
    val locationId = integer("location_id").references(Locations.id)
    val userId = integer("user_id").references(Users.id)
    val priority = integer("priority").default(1)
    val lastStudiedAt = datetime("last_studied_at").nullable()
} 