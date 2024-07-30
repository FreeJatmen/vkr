package org.example.resumeRoute

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureResumeRouteRouting() {
    routing {
        post("/reply_to_vacancy") {
            val resumeRouteController = ResumeRouteController(call)
            resumeRouteController.addResumeRoute()
        }

        get("/view_resume_to_vacancy") {
            val resumeRouteController = ResumeRouteController(call)
            resumeRouteController.viewResumeToVacancy()
        }
    }
}