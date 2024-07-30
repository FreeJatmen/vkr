package org.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.example.addVacancies.configureVacanciesRouting
import org.example.cv.configureResumeRouting
import org.example.login.configureLoginRouting
import org.example.register.configureRegisterRouting
import org.example.resumeRoute.configureResumeRouteRouting
import org.example.route.configureSerialization
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/VKR", "org.postgresql.Driver", "aaaaaaaa", "aaaaaaaa")

    embeddedServer(
        Netty,
        port = 8080) {
            configureLoginRouting()
            configureRegisterRouting()
            configureSerialization()
            configureVacanciesRouting()
            configureResumeRouting()
            configureResumeRouteRouting()
        }
    .start(wait = true)
}


