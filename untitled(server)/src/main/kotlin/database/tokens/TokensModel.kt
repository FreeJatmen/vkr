package org.example.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object TokensModel : Table("tokens") {
    private val id = TokensModel.varchar("id", 50)
    private val login = TokensModel.varchar("login", 25)
    private val token = TokensModel.varchar("token", 50)

    fun insert(tokenDTO: TokensDTO) {
        transaction {
            TokensModel.insert {
                it[id] = tokenDTO.rowId
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }

    fun getLoginByToken(tokenValue: String): String? {
        return transaction {
            TokensModel.select { TokensModel.token eq tokenValue }
                .mapNotNull { it[TokensModel.login] }
                .singleOrNull()
        }
    }
}