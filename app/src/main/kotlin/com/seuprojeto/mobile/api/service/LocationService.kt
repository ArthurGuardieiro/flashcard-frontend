package com.seuprojeto.mobile.api.service

import com.seuprojeto.mobile.api.dto.LocationDTO
import com.seuprojeto.mobile.api.dto.LocationResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface para serviços de localizações
 */
interface LocationService {
    
    @GET("locations")
    suspend fun getLocations(@Query("userId") userId: Int): Response<List<LocationResponse>>
    
    @POST("locations")
    suspend fun createLocation(@Body request: LocationDTO): Response<LocationResponse>
    
    @PUT("locations/{id}")
    suspend fun updateLocation(
        @Path("id") id: Int,
        @Body request: LocationDTO
    ): Response<LocationResponse>
}
