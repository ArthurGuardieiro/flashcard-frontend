package com.example.di

import com.example.models.algorithm.LocationAwareScheduler
import com.example.viewmodel.AuthService
import com.example.viewmodel.FlashcardAnswerService
import com.example.viewmodel.FlashcardService
import com.example.viewmodel.LocationService
import org.koin.dsl.module

val appModule = module {
    single { AuthService() }
    single { FlashcardService() }
    single { FlashcardAnswerService() }
    single { LocationService() }
    single { LocationAwareScheduler(get()) }

    // Adicione outros serviços/repositórios aqui conforme necessário
}