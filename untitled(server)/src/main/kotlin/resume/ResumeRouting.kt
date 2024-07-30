package org.example.cv

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.addVacancies.VacanciesController

fun Application.configureResumeRouting() {
    routing {
        post("/add_resume") {
            val resumeController = ResumeController(call)
            resumeController.addResume()
        }

        get("/get_resume/{id}") {
            val resumeController = ResumeController(call)
            resumeController.getResume()
        }

        get("/view_resume"){
            val resumeController = ResumeController(call)
            resumeController.viewResume()
        }

        delete("delete_resume/{id}") {
            val resumeController = ResumeController(call)
            resumeController.deleteResume()
        }

        post("/update_resume/{id}") {
            val resumeController = ResumeController(call)
            resumeController.updateResume()
        }

        get("/get_resumes"){
            val resumeController = ResumeController(call)
            resumeController.getResumes()
        }
    }
}