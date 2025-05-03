package com.seuprojeto.mobile.model

import java.util.UUID

/**
 * Modelo de dados para armazenar localizações favoritas do usuário.
 * 
 * @property id Identificador único da localização
 * @property name Nome da localização (ex: "Casa", "Biblioteca")
 * @property latitude Latitude da coordenada geográfica
 * @property longitude Longitude da coordenada geográfica
 * @property lastUsed Indica se esta localização foi a última utilizada para estudo
 */
data class Location(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val latitude: Double,
    val longitude: Double,
    var lastUsed: Boolean = false
) {
    /**
     * Calcula a distância entre esta localização e outra fornecida, em metros.
     * 
     * @param otherLocation Localização para calcular a distância
     * @return Distância em metros
     */
    fun distanceTo(otherLocation: Location): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            latitude, longitude,
            otherLocation.latitude, otherLocation.longitude,
            results
        )
        return results[0]
    }
    
    /**
     * Verifica se a localização atual está dentro de um raio específico de outra localização.
     * 
     * @param otherLocation Localização a ser comparada
     * @param radiusInMeters Raio em metros para considerar como "mesma localização"
     * @return true se estiver dentro do raio, false caso contrário
     */
    fun isWithinRadius(otherLocation: Location, radiusInMeters: Float = 100f): Boolean {
        return distanceTo(otherLocation) <= radiusInMeters
    }
} 