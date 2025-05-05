package com.example.viewmodel

import com.example.models.DTO.request.FlashcardDTO
import com.example.models.DTO.request.ReviewDTO
import com.example.models.DTO.response.FlashcardResponse
import com.example.network.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class FlashcardService {
    fun getFlashcards(userId: Int): List<FlashcardResponse> {
        require(userId > 0) { "Invalid user ID" }

        return runBlocking {
            try {
                val response = ApiClient.client.get("${ApiClient.getBaseUrl()}/flashcards?userId=$userId")
                response.body<List<FlashcardResponse>>()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun createFlashcard(request: FlashcardDTO): HttpStatusCode {
        val validatedRequest = request.validate()

        return runBlocking {
            try {
                val response = ApiClient.client.post("${ApiClient.getBaseUrl()}/flashcards") {
                    contentType(ContentType.Application.Json)
                    setBody(validatedRequest)
                }
                response.status
            } catch (e: Exception) {
                HttpStatusCode.InternalServerError
            }
        }
    }

    fun updateFlashcard(id: Int, request: FlashcardDTO): HttpStatusCode {
        require(id > 0) { "Invalid flashcard ID" }
        val validatedRequest = request.validate()

        return runBlocking {
            try {
                val response = ApiClient.client.put("${ApiClient.getBaseUrl()}/flashcards/$id") {
                    contentType(ContentType.Application.Json)
                    setBody(validatedRequest)
                }
                response.status
            } catch (e: Exception) {
                HttpStatusCode.InternalServerError
            }
        }
    }
    
    fun reviewFlashcard(id: Int, userId: Int, review: ReviewDTO): HttpStatusCode {
        require(id > 0) { "Invalid flashcard ID" }
        require(userId > 0) { "Invalid user ID" }
        
        return runBlocking {
            try {
                val response = ApiClient.client.post("${ApiClient.getBaseUrl()}/flashcards/$id/review?userId=$userId") {
                    contentType(ContentType.Application.Json)
                    setBody(review)
                }
                response.status
            } catch (e: Exception) {
                HttpStatusCode.InternalServerError
            }
        }
    }

    // Extensions mantidas como funções privadas da classe
    private fun FlashcardDTO.validate(): FlashcardDTO {
        require(question.isNotBlank()) { "Question cannot be blank" }
        require(answer.isNotBlank()) { "Answer cannot be blank" }
        require(userId > 0) { "Invalid user ID" }
        require(repetitions >= 0) { "Repetitions must be >= 0" }
        require(easinessFactor >= 1.3f) { "Easiness factor must be >= 1.3" }
        require(interval >= 1) { "Interval must be >= 1" }
        return this
    }
}