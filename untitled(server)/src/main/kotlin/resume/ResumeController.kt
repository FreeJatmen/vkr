package org.example.cv

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.example.database.cv.ResumeModel
import org.example.database.cv.ResumeDTO
import org.example.database.tokens.TokensModel.getLoginByToken
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*

class ResumeController (private val call: ApplicationCall) {

    suspend fun addResume() {
        val receive = call.receive<ResumeDTO>()
        val token = call.request.headers["token"]
        try {
            ResumeModel.insert(
                ResumeDTO(
                    rowId = UUID.randomUUID().toString(),
                    login = getLoginByToken(token.toString()).toString(),
                    name = receive.name,
                    surname = receive.surname,
                    contacts = receive.contacts,
                    skills = receive.skills,
                    aboutYourself = receive.aboutYourself,
                    resumeName = receive.resumeName
                )
            )
            call.respond(HttpStatusCode.OK)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict)
        }
    }

    suspend fun viewResume() {
        val token = call.request.headers["token"]

        try {
            val login = getLoginByToken(token.toString())
            val resume = ResumeModel.getResumeByLogin(login.toString())

            call.respond(resume)

        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }

    suspend fun deleteResume() {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
        try {
            ResumeModel.deleteResume(id)
            call.respond(HttpStatusCode.OK, "Resume deleted successfully")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to delete resume")
        }
    }

    suspend fun updateResume() {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
        val receive = call.receive<ResumeDTO>()
        val token = call.request.headers["token"]
        try {
            val login = getLoginByToken(token.toString())
            ResumeModel.updateResume(
                id = id,
                newValues = ResumeDTO(
                    rowId = id,
                    login = login.toString(),
                    name = receive.name,
                    surname = receive.surname,
                    contacts = receive.contacts,
                    skills = receive.skills,
                    aboutYourself = receive.aboutYourself,
                    resumeName = receive.resumeName
                )
            )
            call.respond(HttpStatusCode.OK, "Vacancy updated successfully")
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "Failed to update vacancy due to database conflict")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to update vacancy")
        }
    }

    suspend fun getResume() {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
        val token = call.request.headers["token"]
        try {
            getLoginByToken(token.toString())
            val resume = ResumeModel.getResume(id)
            call.respond(resume)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }

    suspend fun getResumes() {
        val token = call.request.headers["token"]
        try {
            val login = getLoginByToken(token.toString()).toString()
            val resume = ResumeModel.getResumeByLogin(login)
            call.respond(resume)
            println(resume)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }
}