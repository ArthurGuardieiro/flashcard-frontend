package com.example.viewmodel

import com.example.models.DTO.request.FlashcardAnswerDTO
import com.example.models.DTO.response.FlashcardAnswerResponse
import com.example.network.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class FlashcardAnswerService {
    fun getFlashcardAnswers(flashcardId: Int): List<FlashcardAnswerResponse> {
        require(flashcardId > 0) { "Invalid flashcard ID" }

        return runBlocking {
            try {
                val response = ApiClient.client.get("${ApiClient.getBaseUrl()}/flashcard-answers?flashcardId=$flashcardId")
                response.body<List<FlashcardAnswerResponse>>()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun createFlashcardAnswer(request: FlashcardAnswerDTO): HttpStatusCode {
        require(request.flashcardId > 0) { "Invalid flashcard ID" }
        require(request.userId > 0) { "Invalid user ID" }

        return runBlocking {
            try {
                val response = ApiClient.client.post("${ApiClient.getBaseUrl()}/flashcard-answers") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                response.status
            } catch (e: Exception) {
                HttpStatusCode.InternalServerError
            }
        }
    }
}