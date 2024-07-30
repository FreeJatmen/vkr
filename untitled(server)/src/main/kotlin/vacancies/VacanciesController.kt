package org.example.addVacancies

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.example.database.tokens.TokensModel.getLoginByToken
import org.example.database.vacancies.VacanciesModel
import org.example.database.vacancies.VacanciesDTO
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*

class VacanciesController (private val call: ApplicationCall) {

    suspend fun addVacancies() {
        val receive = call.receive<VacanciesRemote>()
        val token = call.request.headers["token"]

        try {
            VacanciesModel.insert(
                VacanciesDTO(
                    rowId = UUID.randomUUID().toString(),
                    login = getLoginByToken(token.toString()).toString(),
                    name = receive.name,
                    payment = receive.payment,
                    expertise = receive.expertise,
                    employment = receive.employment,
                    description = receive.description
                )
            )
            call.respond(HttpStatusCode.OK)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict)
        }
    }

    suspend fun viewLoginVacancies() {
        val token = call.request.headers["token"]

        try {
            val login = getLoginByToken(token.toString())
            val vacancies = VacanciesModel.getVacanciesByLogin(login.toString())

            call.respond(vacancies)

        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }

    suspend fun viewVacancies() {
        try {
            val vacancies = VacanciesModel.getVacancies()
            call.respond(vacancies)

        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }

    suspend fun deleteVacancy() {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
        try {
            VacanciesModel.deleteVacancy(id)
            call.respond(HttpStatusCode.OK, "Vacancy deleted successfully")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to delete vacancy")
        }
    }

    suspend fun searchVacancies() {
        val query = call.request.queryParameters["query"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing query parameter")
        val result = VacanciesModel.search(query)
        call.respond(result)
    }

    suspend fun updateVacancy() {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
        val receive = call.receive<VacanciesRemote>()
        val token = call.request.headers["token"]
        try {
            val login = getLoginByToken(token.toString())
            VacanciesModel.updateVacancy(
                id = id,
                newValues = VacanciesDTO(
                    rowId = id,
                    login = login.toString(),
                    name = receive.name,
                    payment = receive.payment,
                    expertise = receive.expertise,
                    employment = receive.employment,
                    description = receive.description
                )
            )
            call.respond(HttpStatusCode.OK, "Vacancy updated successfully")
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "Failed to update vacancy due to database conflict")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to update vacancy")
        }
    }

    suspend fun getAuthVacancy() {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
        val token = call.request.headers["token"]
        try {
            getLoginByToken(token.toString())
            val vacancy = VacanciesModel.getVacancy(id)
            call.respond(vacancy)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }

    suspend fun getVacancy() {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
        try {
            val vacancy = VacanciesModel.getVacancy(id)
            call.respond(vacancy)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }
}