package com.seuprojeto.mobile.api.service

import com.seuprojeto.mobile.api.dto.CreateFlashcardDTO
import com.seuprojeto.mobile.api.dto.FlashcardResponse
import com.seuprojeto.mobile.api.dto.ReviewDTO
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface para serviços de flashcards
 */
interface FlashcardService {
    
    @GET("flashcards/user")
    suspend fun getFlashcardsByUserId(@Query("userId") userId: Int): Response<List<FlashcardResponse>>
    
    @GET("flashcards/deck")
    suspend fun getFlashcardsByDeckId(@Query("deckId") deckId: Int): Response<List<FlashcardResponse>>
    
    @POST("flashcards")
    suspend fun createFlashcard(@Body request: CreateFlashcardDTO): Response<Void>
    
    @PUT("flashcards/{id}")
    suspend fun updateFlashcard(
        @Path("id") id: Int,
        @Body request: CreateFlashcardDTO
    ): Response<Void>
    
    @POST("flashcards/{id}/review")
    suspend fun reviewFlashcard(
        @Path("id") id: Int,
        @Query("userId") userId: Int,
        @Body review: ReviewDTO
    ): Response<Void>
}
