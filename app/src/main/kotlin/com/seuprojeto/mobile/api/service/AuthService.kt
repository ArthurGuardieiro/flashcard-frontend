package com.seuprojeto.mobile.api.service

import com.seuprojeto.mobile.api.dto.AuthResponse
import com.seuprojeto.mobile.api.dto.LoginResponse
import com.seuprojeto.mobile.api.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interface para serviços de autenticação
 */
interface AuthService {
    
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("login")
    suspend fun login(@Body request: RegisterRequest): Response<LoginResponse>
}
