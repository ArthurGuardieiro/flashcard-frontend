package com.example.models.entities

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Users : IntIdTable() {
    val username = varchar("username", 50).uniqueIndex()
    val passwordHash = varchar("password_hash", 256)
    val defaultLocationId = integer("default_location_id").default(1)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
} 