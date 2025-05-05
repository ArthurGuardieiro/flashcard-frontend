package com.example.viewmodel

import com.example.models.DTO.request.AuthResponse
import com.example.models.DTO.request.RegisterRequest
import com.example.models.DTO.response.LoginResponse
import com.example.network.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class AuthService {
    fun register(request: RegisterRequest): Pair<AuthResponse, HttpStatusCode> {
        require(request.username.isNotBlank()) { "Username cannot be blank" }
        require(request.password.length >= 6) { "Password must be at least 6 characters" }

        return runBlocking {
            try {
                val response = ApiClient.client.post("${ApiClient.getBaseUrl()}/register") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                
                val authResponse = response.body<AuthResponse>()
                Pair(authResponse, response.status)
            } catch (e: Exception) {
                Pair(AuthResponse("Erro ao se conectar ao servidor: ${e.message}"), HttpStatusCode.InternalServerError)
            }
        }
    }

    fun login(request: RegisterRequest): Pair<LoginResponse, HttpStatusCode> {
        require(request.username.isNotBlank()) { "Username cannot be blank" }
        require(request.password.isNotBlank()) { "Password cannot be blank" }

        return runBlocking {
            try {
                val response = ApiClient.client.post("${ApiClient.getBaseUrl()}/login") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                
                val loginResponse = response.body<LoginResponse>()
                Pair(loginResponse, response.status)
            } catch (e: Exception) {
                Pair(
                    LoginResponse(
                        message = "Erro ao se conectar ao servidor: ${e.message}",
                        userId = null
                    ),
                    HttpStatusCode.InternalServerError
                )
            }
        }
    }
}