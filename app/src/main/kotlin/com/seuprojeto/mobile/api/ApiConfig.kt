package com.seuprojeto.mobile.api

/**
 * Configuração da API do backend
 */
object ApiConfig {
    // URL base do servidor backend
    const val BASE_URL = "http://10.0.2.2:8080/api"
    
    // Endpoints da API
    object Endpoints {
        // Auth
        const val REGISTER = "$BASE_URL/register"
        const val LOGIN = "$BASE_URL/login"
        
        // Flashcards
        const val FLASHCARDS = "$BASE_URL/flashcards"
        const val FLASHCARDS_BY_USER = "$BASE_URL/flashcards/user"
        const val FLASHCARDS_BY_DECK = "$BASE_URL/flashcards/deck"
        const val FLASHCARD_REVIEW = "$BASE_URL/flashcards/{id}/review"
        
        // Flashcard Answers
        const val FLASHCARD_ANSWERS = "$BASE_URL/flashcards/answer"
        const val FLASHCARD_ANSWERS_BY_USER = "$BASE_URL/flashcards/answers"
        
        // Locations
        const val LOCATIONS = "$BASE_URL/locations"
        
        // Decks
        const val DECKS = "$BASE_URL/decks"
        const val DECKS_BY_ID = "$BASE_URL/decks/{id}"
        const val DECKS_OTHERS = "$BASE_URL/decks/others"
    }
}
