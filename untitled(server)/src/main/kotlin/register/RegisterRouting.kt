package org.example.register

import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRegisterRouting() {

    routing {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }

        post("/change_password") {
            val registerController = RegisterController(call)
            registerController.changePassword()
        }
    }
}