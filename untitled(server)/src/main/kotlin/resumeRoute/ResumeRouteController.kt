package org.example.resumeRoute

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.example.database.cv.ResumeModel.getResumesByIdList
import org.example.database.resumeRoute.ResumeRouteModel
import org.example.database.resumeRoute.ResumeRouteModel.getResumeByVacancy
import org.example.database.resumeRoute.ResumeRouteDTO
import org.example.database.tokens.TokensModel.getLoginByToken
import org.example.database.vacancies.VacanciesModel.getLoginByVacancy
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*

class ResumeRouteController(private val call: ApplicationCall) {

    suspend fun addResumeRoute() {
        val receive = call.receive<ResumeRouteRemote>()
        val token = call.request.headers["token"]
        try {
            val loginResume = getLoginByToken(token.toString())
            ResumeRouteModel.insert(
                ResumeRouteDTO(
                    rowId = UUID.randomUUID().toString(),
                    loginResume = loginResume.toString(),
                    loginVacancy = getLoginByVacancy(receive.idVacancy),
                    idResume = receive.idResume,
                    idVacancy = receive.idVacancy
                )
            )
            call.respond(HttpStatusCode.OK)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict)
        }
    }

    suspend fun viewResumeToVacancy() {
        val token = call.request.headers["token"]
        val id = call.request.headers["id"]
        try {
            getLoginByToken(token.toString())
            val resumeVacancy = getResumeByVacancy(id.toString())
            val result = getResumesByIdList(resumeVacancy)
            call.respond(result)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
    }
}