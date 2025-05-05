package com.seuprojeto.mobile.api.service

import com.seuprojeto.mobile.api.dto.DeckDTO
import com.seuprojeto.mobile.api.dto.DeckResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface para serviços de decks
 */
interface DeckService {
    
    @GET("decks")
    suspend fun getDecks(@Query("userId") userId: Int): Response<List<DeckResponse>>
    
    @GET("decks/{id}")
    suspend fun getDeck(@Path("id") id: Int): Response<DeckResponse>
    
    @GET("decks/others")
    suspend fun getOthersDecks(@Query("userId") userId: Int): Response<List<DeckResponse>>
    
    @POST("decks")
    suspend fun createDeck(
        @Body request: DeckDTO,
        @Query("userId") userId: Int
    ): Response<Void>
    
    @PUT("decks/{id}")
    suspend fun updateDeck(
        @Path("id") id: Int,
        @Body request: DeckDTO
    ): Response<Void>
    
    @DELETE("decks/{id}")
    suspend fun deleteDeck(@Path("id") id: Int): Response<Void>
}
