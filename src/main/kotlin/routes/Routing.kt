package com.example.routes

fun Route.authRoutes() {
    post("/register") {
        val request = call.receive<RegisterRequest>().apply {
            require(username.isNotBlank()) { "Username cannot be blank" }
            require(password.length >= 6) { "Password must be at least 6 characters" }
        }

        val passwordHash = request.password.hashCode().toString()

        val exists = transaction {
            Users.select { Users.username eq request.username }.any()
        }

        if (exists) {
            call.respond(HttpStatusCode.Conflict, AuthResponse("Usuário já existe"))
            return@post
        }

        transaction {
            Users.insert {
                it[Users.username] = request.username
                it[Users.passwordHash] = passwordHash
            }
        }

        call.respond(HttpStatusCode.Created, AuthResponse("Usuário registrado com sucesso"))
    }

    post("/login") {
        val request = call.receive<RegisterRequest>().apply {
            require(username.isNotBlank()) { "Username cannot be blank" }
            require(password.isNotBlank()) { "Password cannot be blank" }
        }

        val passwordHash = request.password.hashCode().toString()

        val user = transaction {
            Users.select {
                Users.username eq request.username and (Users.passwordHash eq passwordHash)
            }.singleOrNull()
        }

        if (user != null) {
            call.respond(LoginResponse("Login bem-sucedido", user[Users.id]))
        } else {
            call.respond(HttpStatusCode.Unauthorized, AuthResponse("Credenciais inválidas"))
        }
    }
}