package com.example.viewmodel

import com.example.models.entities.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class LocationService {
    fun getDefaultLocation(userId: Int): Int {
        return transaction {
            val user = Users.select { Users.id eq userId }.singleOrNull()
                ?: throw IllegalArgumentException("Usuário não encontrado")

            user[Users.defaultLocationId].takeIf { it > 0 }
                ?: throw IllegalStateException("Local padrão não configurado")
        }
    }
    fun setDefaultLocation(userId: Int, locationId: Int) {
        transaction {
            Users.update({ Users.id eq userId }) {
                it[defaultLocationId] = locationId
            }
        }
    }
}