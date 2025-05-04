package com.example.viewmodel

import com.example.models.DTO.request.FlashcardDTO
import com.example.models.DTO.response.FlashcardResponse
import com.example.models.entities.Flashcards
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class FlashcardService {
    fun getFlashcards(userId: Int): List<FlashcardResponse> {
        require(userId > 0) { "Invalid user ID" }

        return transaction {
            Flashcards.select { Flashcards.userId eq userId }
                .map { it.toFlashcardResponse() }
        }
    }

    fun createFlashcard(request: FlashcardDTO): HttpStatusCode {
        val validatedRequest = request.validate()

        transaction {
            Flashcards.insert {
                it.applyFromDTO(validatedRequest)
            }
        }
        return HttpStatusCode.Created
    }

    fun updateFlashcard(id: Int, request: FlashcardDTO): HttpStatusCode {
        require(id > 0) { "Invalid flashcard ID" }
        val validatedRequest = request.validate()

        transaction {
            Flashcards.update({ Flashcards.id eq id }) {
                it.applyFromDTO(validatedRequest)
            }
        }
        return HttpStatusCode.OK
    }

    // Extensions mantidas como funções privadas da classe
    private fun ResultRow.toFlashcardResponse(): FlashcardResponse {
        return FlashcardResponse(
            id = this[Flashcards.id].value,
            question = this[Flashcards.question],
            answer = this[Flashcards.answer],
            type = this[Flashcards.type].toString(),
            options = this[Flashcards.options]?.split(";") ?: emptyList(),
            nextRepetition = this[Flashcards.nextRepetition],
            repetitions = this[Flashcards.repetitions],
            easinessFactor = this[Flashcards.easinessFactor],
            interval = this[Flashcards.interval]
        )
    }

    private fun FlashcardDTO.validate(): FlashcardDTO {
        require(question.isNotBlank()) { "Question cannot be blank" }
        require(answer.isNotBlank()) { "Answer cannot be blank" }
        require(userId > 0) { "Invalid user ID" }
        require(repetitions >= 0) { "Repetitions must be >= 0" }
        require(easinessFactor >= 1.3f) { "Easiness factor must be >= 1.3" }
        require(interval >= 1) { "Interval must be >= 1" }
        return this
    }

    private fun InsertStatement<Number>.applyFromDTO(dto: FlashcardDTO) {
        this[Flashcards.question] = dto.question
        this[Flashcards.answer] = dto.answer
        this[Flashcards.type] = dto.type
        this[Flashcards.options] = dto.options?.joinToString(";")
        this[Flashcards.userId] = dto.userId
        this[Flashcards.deckId] = dto.deckId
        this[Flashcards.nextRepetition] = dto.nextRepetition.toString()
        this[Flashcards.repetitions] = dto.repetitions
        this[Flashcards.easinessFactor] = dto.easinessFactor
        this[Flashcards.interval] = dto.interval
    }

    private fun UpdateStatement.applyFromDTO(dto: FlashcardDTO) {
        this[Flashcards.question] = dto.question
        this[Flashcards.answer] = dto.answer
        this[Flashcards.type] = dto.type
        this[Flashcards.options] = dto.options?.joinToString(";")
        this[Flashcards.userId] = dto.userId
        this[Flashcards.deckId] = dto.deckId
        this[Flashcards.nextRepetition] = dto.nextRepetition.toString()
        this[Flashcards.repetitions] = dto.repetitions
        this[Flashcards.easinessFactor] = dto.easinessFactor
        this[Flashcards.interval] = dto.interval
    }
}