package com.example.viewmodel

import com.example.models.DTO.request.LocationDTO
import com.example.models.DTO.response.LocationResponse
import com.example.network.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

class LocationService {
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
}