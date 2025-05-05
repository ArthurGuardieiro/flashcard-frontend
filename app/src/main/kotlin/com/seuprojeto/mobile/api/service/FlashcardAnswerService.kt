package com.seuprojeto.mobile.api.service

import com.seuprojeto.mobile.api.dto.FlashcardAnswerDTO
import com.seuprojeto.mobile.api.dto.FlashcardAnswerResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface para serviços de respostas de flashcards
 */
interface FlashcardAnswerService {
    
    @POST("flashcards/answer")
    suspend fun processAnswer(@Body request: FlashcardAnswerDTO): Response<Void>
    
    @GET("flashcards/answers")
    suspend fun getByUserId(@Query("userId") userId: Int): Response<List<FlashcardAnswerResponse>>
}
