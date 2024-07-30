package org.example.database.vacancies

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object VacanciesModel: Table("vacanciestable") {
    private val rowId = VacanciesModel.varchar("id", 50)
    private val login = VacanciesModel.varchar("login", 25)
    private val name = VacanciesModel.varchar("name", 100)
    private val payment = VacanciesModel.varchar("payment", 50)
    private val expertise = VacanciesModel.varchar("expertise", 50)
    private val employment = VacanciesModel.varchar("employment", 50)
    private val description = VacanciesModel.varchar("description", 2000)

    fun insert(vacanciesDTO: VacanciesDTO) {
        transaction {
            VacanciesModel.insert {
                it[rowId] = vacanciesDTO.rowId
                it[login] = vacanciesDTO.login
                it[name] = vacanciesDTO.name
                it[payment] = vacanciesDTO.payment
                it[expertise] = vacanciesDTO.expertise
                it[employment] = vacanciesDTO.employment
                it[description] = vacanciesDTO.description
            }
        }
    }

    fun getVacanciesByLogin(loginValue: String): List<VacanciesDTO> {
        return transaction {
            VacanciesModel.select { login eq loginValue }.toList().map {
                VacanciesDTO(
                    rowId = it[rowId],
                    login = it[login],
                    name = it[name],
                    payment = it[payment],
                    expertise = it[expertise],
                    employment = it[employment],
                    description = it[description]
                )
            }
        }
    }

    fun getVacancies(): List<VacanciesDTO> {
        return transaction {
            VacanciesModel.selectAll() .toList().map {
                VacanciesDTO(
                    rowId = it[VacanciesModel.rowId],
                    login = it[VacanciesModel.login],
                    name = it[VacanciesModel.name],
                    payment = it[VacanciesModel.payment],
                    expertise = it[VacanciesModel.expertise],
                    employment = it[VacanciesModel.employment],
                    description = it[VacanciesModel.description]
                )
            }
        }
    }

    fun updateVacancy(id: String, newValues: VacanciesDTO) {
        transaction {
            VacanciesModel.update({ VacanciesModel.rowId eq id }) {
                it[name] = newValues.name
                it[payment] = newValues.payment
                it[expertise] = newValues.expertise
                it[employment] = newValues.employment
                it[description] = newValues.description
            }
        }
    }

    fun getLoginByVacancy(id: String): String {
        return transaction {
            VacanciesModel
                .slice(login)
                .select { rowId eq id }
                .map { it[login] }
                .single()
        }
    }


    fun getVacancy(id: String): VacanciesDTO{
        return transaction {
            VacanciesModel.select { VacanciesModel.rowId eq id }.single().let {
                VacanciesDTO(
                    rowId = it[VacanciesModel.rowId],
                    login = it[VacanciesModel.login],
                    name = it[VacanciesModel.name],
                    payment = it[VacanciesModel.payment],
                    expertise = it[VacanciesModel.expertise],
                    employment = it[VacanciesModel.employment],
                    description = it[VacanciesModel.description]
                )
            }
        }
    }

    fun deleteVacancy(id: String) {
        transaction {
            VacanciesModel.deleteWhere { VacanciesModel.rowId eq id }
        }
    }

    fun search(query: String): List<VacanciesDTO> {
        return transaction {
            VacanciesModel.select {
                (VacanciesModel.name like "%$query%") or
                        (VacanciesModel.description like "%$query%")
            }.map {
                VacanciesDTO(
                    rowId = it[VacanciesModel.rowId],
                    login = it[VacanciesModel.login],
                    name = it[VacanciesModel.name],
                    payment = it[VacanciesModel.payment],
                    expertise = it[VacanciesModel.expertise],
                    employment = it[VacanciesModel.employment],
                    description = it[VacanciesModel.description]
                )
            }
        }
    }
}