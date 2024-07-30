package org.example.database.resumeRoute

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object ResumeRouteModel : Table("resumeroutetable") {
    private val rowId = ResumeRouteModel.varchar("id", 50)
    private val loginResume = ResumeRouteModel.varchar("login_resume", 25)
    private val loginVacancy = ResumeRouteModel.varchar("login_vacancy", 25)
    private val idResume = ResumeRouteModel.varchar("id_resume", 50)
    private val idVacancy = ResumeRouteModel.varchar("id_vacancy", 50)

    fun insert(resumeRoute: ResumeRouteDTO) {
        transaction {
            ResumeRouteModel.insert {
                it[rowId] = resumeRoute.rowId
                it[loginResume] = resumeRoute.loginResume
                it[loginVacancy] = resumeRoute.loginVacancy
                it[idResume] = resumeRoute.idResume
                it[idVacancy] = resumeRoute.idVacancy
            }
        }
    }

    fun getResumeByVacancy(idVacancy: String): List<String> {
        return transaction {
            ResumeRouteModel
                .select { ResumeRouteModel.idVacancy eq idVacancy }
                .map { it[ResumeRouteModel.idResume] }
        }
    }
}