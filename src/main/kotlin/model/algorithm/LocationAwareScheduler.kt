package com.example.models.algorithm

import com.example.models.entities.FlashcardAnswer
import com.example.models.entities.FlashcardLocationPriority
import com.example.models.entities.FlashcardLocationPriority.priority
import com.example.models.entities.Users
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert
import com.example.viewmodel.LocationService

class LocationAwareScheduler(private val locationService: LocationService) {

    fun getNextReviewLocation(flashcardId: Int, userId: Int): Int {
        return transaction {
            FlashcardAnswer
                .slice(FlashcardAnswer.locationId)
                .select { FlashcardAnswer.flashcardId eq flashcardId }
                .orderBy(FlashcardAnswer.createdAt to SortOrder.ASC)
                .limit(1)
                .singleOrNull()
                ?.get(FlashcardAnswer.locationId)
                ?: locationService.getDefaultLocation(userId)
        }
    }

    fun updateLocationPriority(flashcardId: Int, locationId: Int) {
        transaction {
            // Aumenta a prioridade (valor) de todos os outros locais
            FlashcardLocationPriority.update(
                where = { FlashcardLocationPriority.flashcardId eq flashcardId }
            ) {
                with(SqlExpressionBuilder) {
                    it.update(priority, priority + 1)
                }
            }

            // Define prioridade máxima (0) para o local atual
            FlashcardLocationPriority.upsert {
                it[FlashcardLocationPriority.flashcardId] = flashcardId
                it[FlashcardLocationPriority.locationId] = locationId
                it[priority] = 0
            }
        }
    }
}