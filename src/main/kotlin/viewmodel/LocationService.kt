package com.example.viewmodel

import com.example.models.DTO.request.LocationDTO
import com.example.models.DTO.response.LocationResponse
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

class LocationService {
    // Armazenamento local de localização padrão para o usuário atual
    private var currentDefaultLocationId: Int = 1  // Valor padrão inicial arbitrário
    
    fun getLocations(): List<LocationResponse> {
        return runBlocking {
            try {
                val response = ApiClient.client.get("${ApiClient.getBaseUrl()}/locations")
                response.body<List<LocationResponse>>()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun createLocation(request: LocationDTO): HttpStatusCode {
        require(request.name.isNotBlank()) { "Location name cannot be blank" }

        return runBlocking {
            try {
                val response = ApiClient.client.post("${ApiClient.getBaseUrl()}/locations") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                response.status
            } catch (e: Exception) {
                HttpStatusCode.InternalServerError
            }
        }
    }

    fun getDefaultLocation(userId: Int): Int {
        // Tenta obter a localização padrão do usuário do servidor
        return runBlocking {
            try {
                val response = ApiClient.client.get("${ApiClient.getBaseUrl()}/users/$userId/default-location")
                val locationId = response.body<Int>()
                // Atualiza o armazenamento local
                currentDefaultLocationId = locationId
                locationId
            } catch (e: Exception) {
                // Retorna a localização padrão atual do cliente em caso de falha
                currentDefaultLocationId
            }
        }
    }
    
    fun setDefaultLocation(userId: Int, locationId: Int): HttpStatusCode {
        // Atualiza localmente
        currentDefaultLocationId = locationId
        
        // Envia a atualização para o servidor
        return runBlocking {
            try {
                val response = ApiClient.client.put("${ApiClient.getBaseUrl()}/users/$userId/default-location") {
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("locationId" to locationId))
                }
                response.status
            } catch (e: Exception) {
                HttpStatusCode.InternalServerError
            }
        }
    }
}