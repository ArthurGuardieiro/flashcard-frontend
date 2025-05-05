package models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object Decks : IntIdTable() {
    val name = varchar("name", 255)
    val description = text("description")
    val userId = integer("user_id")
}
