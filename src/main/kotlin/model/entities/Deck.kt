package com.example.model.entities

import com.example.models.entities.FlashcardAnswer.nullable
import org.jetbrains.exposed.dao.id.IntIdTable

object Deck: IntIdTable() {
    val name = text("deck_name").nullable()
}