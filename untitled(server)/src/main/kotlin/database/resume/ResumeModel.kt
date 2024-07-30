package org.example.database.cv


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object ResumeModel : Table("resumetable") {
    private val rowId = ResumeModel.varchar("id", 50)
    private val login = ResumeModel.varchar("login", 25)
    private val name = ResumeModel.varchar("name", 50)
    private val surname = ResumeModel.varchar("surname", 50)
    private val contacts = ResumeModel.varchar("contacts", 50)
    private val skills = ResumeModel.varchar("skills", 500)
    private val aboutYourself = ResumeModel.varchar("about_yourself", 2000)
    private val resumeName = ResumeModel.varchar("resume_name", 50)

    fun insert(resumeDTO: ResumeDTO) {
        transaction {
            ResumeModel.insert {
                it[rowId] = resumeDTO.rowId
                it[login] = resumeDTO.login
                it[name] = resumeDTO.name
                it[surname] = resumeDTO.surname
                it[contacts] = resumeDTO.contacts
                it[skills] = resumeDTO.skills
                it[aboutYourself] = resumeDTO.aboutYourself
                it[resumeName] = resumeDTO.resumeName
            }
        }
    }

    fun getResume(id: String): ResumeDTO {
        return transaction {
            ResumeModel.select { rowId eq id }.single().let {
                ResumeDTO(
                    rowId = it[rowId],
                    login = it[login],
                    name = it[name],
                    surname = it[surname],
                    contacts = it[contacts],
                    skills = it[skills],
                    aboutYourself = it[aboutYourself],
                    resumeName = it[resumeName]
                )
            }
        }
    }

    fun getResumeByLogin(loginValue: String): List<ResumeDTO> {
        return transaction {
            ResumeModel.select { login eq loginValue }.toList().map {
                ResumeDTO(
                    rowId = it[rowId],
                    login = it[login],
                    name = it[name],
                    surname = it[surname],
                    contacts = it[contacts],
                    skills = it[skills],
                    aboutYourself = it[aboutYourself],
                    resumeName = it[resumeName]
                )
            }
        }
    }

    fun updateResume(id: String, newValues: ResumeDTO) {
        transaction {
            ResumeModel.update({ ResumeModel.rowId eq id }) {
                it[name] = newValues.name
                it[surname] = newValues.surname
                it[contacts] = newValues.contacts
                it[skills] = newValues.skills
                it[aboutYourself] = newValues.aboutYourself
                it[resumeName] = newValues.resumeName
            }
        }
    }

    fun getResumesByIdList(idList: List<String>): List<ResumeDTO> {
        return transaction {
            ResumeModel.select { ResumeModel.rowId inList idList }.toList().map {
                ResumeDTO(
                    rowId = it[rowId],
                    login = it[login],
                    name = it[name],
                    surname = it[surname],
                    contacts = it[contacts],
                    skills = it[skills],
                    aboutYourself = it[aboutYourself],
                    resumeName = it[resumeName]
                )
            }
        }
    }


    fun deleteResume(id: String) {
        transaction {
            ResumeModel.deleteWhere { rowId eq id }
        }
    }
}