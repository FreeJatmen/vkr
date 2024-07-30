package org.example.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


object UserModel: Table("userstable") {
    private val login = UserModel.varchar("login", 25)
    private val password = UserModel.varchar("password", 25)

    fun insert(userDTO: UserDTO) {
        transaction {
            UserModel.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
            }
        }
    }

    fun updatePassword(loginByToken: String, userDTO: UserDTO) {
        transaction {
            UserModel.update({ UserModel.login eq loginByToken }) {
                it[login] = userDTO.login
                it[password] = userDTO.password
            }
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return transaction {
            val resultRow = UserModel.select { UserModel.login eq login }.singleOrNull()
            resultRow?.let {
                UserDTO(
                    login = it[UserModel.login],
                    password = it[password]
                )
            }
        }
    }
}
