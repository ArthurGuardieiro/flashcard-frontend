package com.seuprojeto.mobile.api.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para comunicação com a API
 */
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val contentType = "application/json".toMediaType()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
    
    // Serviços
    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
    
    val flashcardService: FlashcardService by lazy {
        retrofit.create(FlashcardService::class.java)
    }
    
    val flashcardAnswerService: FlashcardAnswerService by lazy {
        retrofit.create(FlashcardAnswerService::class.java)
    }
    
    val locationService: LocationService by lazy {
        retrofit.create(LocationService::class.java)
    }
    
    val deckService: DeckService by lazy {
        retrofit.create(DeckService::class.java)
    }
}
