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
} 