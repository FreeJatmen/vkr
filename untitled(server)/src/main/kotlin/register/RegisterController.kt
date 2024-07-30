package org.example.register

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.example.database.users.UserDTO
import org.example.database.users.UserModel
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*
import io.ktor.http.*
import org.example.database.tokens.TokensDTO
import org.example.database.tokens.TokensModel
import org.example.database.tokens.TokensModel.getLoginByToken

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        val userDTO = UserModel.fetchUser(registerReceiveRemote.login)
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()
            try {
                UserModel.insert(
                    UserDTO(
                        login = registerReceiveRemote.login,
                        password = registerReceiveRemote.password
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Can't create user")
            }
            TokensModel.insert(
                TokensDTO(
                    rowId = UUID.randomUUID().toString(),
                    login = registerReceiveRemote.login,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }
    }

    suspend fun changePassword() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        val token = call.request.headers["token"]
        val newPassword = call.request.headers["newPassword"]

        try {
            val loginByToken = getLoginByToken(token.toString())

            if (loginByToken == registerReceiveRemote.login) {
                UserModel.updatePassword(loginByToken,
                    UserDTO(
                        login = loginByToken,
                        password = newPassword.toString()
                    )
                )
                call.respond(HttpStatusCode.OK, "Password changed successfully")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid login")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to change password: ${e.message}")
        }
    }
}