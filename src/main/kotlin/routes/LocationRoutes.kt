package com.example.routes

import com.example.models.DTO.request.LocationDTO
import com.example.viewmodel.LocationService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.locationRoutes(locationService: LocationService) {
    get("/locations") {
        val locations = locationService.getLocations()
        call.respond(locations)
    }

    post("/locations") {
        try {
            val request = call.receive<LocationDTO>()
            val status = locationService.createLocation(request)
            call.respond(status)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid data")))
        }
    }
    
    get("/users/{userId}/default-location") {
        try {
            val userId = call.parameters["userId"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
            
            val locationId = locationService.getDefaultLocation(userId)
            call.respond(locationId)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "An error occurred")))
        }
    }
    
    put("/users/{userId}/default-location") {
        try {
            val userId = call.parameters["userId"]?.toIntOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
            
            val request = call.receive<Map<String, Int>>()
            val locationId = request["locationId"]
                ?: return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing locationId"))
            
            val status = locationService.setDefaultLocation(userId, locationId)
            call.respond(status)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "An error occurred")))
        }
    }
} 