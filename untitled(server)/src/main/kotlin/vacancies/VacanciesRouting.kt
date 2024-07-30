package org.example.addVacancies

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.login.LoginController

fun Application.configureVacanciesRouting() {
    routing {
        post("/add_vacancies") {
            val vacanciesController = VacanciesController(call)
            vacanciesController.addVacancies()
        }

        get("/view_login_vacancies") {
            val vacanciesController = VacanciesController(call)
            vacanciesController.viewLoginVacancies()
        }

        get("/view_vacancies") {
            val vacanciesController = VacanciesController(call)
            vacanciesController.viewVacancies()
        }

        delete("delete_vacancies/{id}") {
            val vacanciesController = VacanciesController(call)
            vacanciesController.deleteVacancy()
        }

        get("/search_vacancies") {
            val vacanciesController = VacanciesController(call)
            vacanciesController.searchVacancies()
        }

        get("/get_auth_vacancy/{id}") {
            val vacanciesController = VacanciesController(call)
            vacanciesController.getAuthVacancy()
        }

        get("/get_vacancy/{id}") {
            val vacanciesController = VacanciesController(call)
            vacanciesController.getVacancy()
        }

        post("/update_vacancy/{id}") {
            val vacanciesController = VacanciesController(call)
            vacanciesController.updateVacancy()
        }
    }
}