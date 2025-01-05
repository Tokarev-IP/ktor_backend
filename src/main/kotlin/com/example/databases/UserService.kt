package com.example.databases

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserService(
    database: Database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
) : UserServiceInterface {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val email = varchar("email", length = 50).uniqueIndex()
        val password = varchar("password", length = 50)
        val accessToken = varchar("access_token", length = 70).uniqueIndex()
        val tokenTime = long("token_time")

        override val primaryKey = PrimaryKey(email)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    override suspend fun createNewUser(user: ExposedUser, token: String, time: Long): String {
        return dbQuery {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
                it[password] = user.password
                it[accessToken] = token
                it[tokenTime] = time
            }[Users.accessToken]
        }
    }

    override suspend fun readUserByEmail(email: String): ExposedUser? {
        return dbQuery {
            Users.selectAll()
                .where { Users.email eq email }
                .map { ExposedUser(it[Users.name], it[Users.email], it[Users.password]) }
                .singleOrNull()
        }
    }

    override suspend fun readAllUsers(): List<ExposedUser> {
        return dbQuery {
            Users.selectAll()
                .map { ExposedUser(it[Users.name], it[Users.email], it[Users.password]) }
        }
    }

    override suspend fun checkUserByToken(accessToken: String): ExposedUserToken? {
        return dbQuery {
            Users.selectAll()
                .where { Users.accessToken eq accessToken }
                .map { ExposedUserToken(it[Users.accessToken], it[Users.tokenTime]) }
                .singleOrNull()
        }
    }

    override suspend fun checkUserByEmail(email: String): ExposedUserToken? {
        return dbQuery {
            Users.selectAll()
                .where { Users.email eq email }
                .map { ExposedUserToken(it[Users.accessToken], it[Users.tokenTime]) }
                .singleOrNull()
        }
    }

    override suspend fun checkUserByEmailAndPassword(email: String, password: String): ExposedUserToken? {
        return dbQuery {
            Users.selectAll()
                .where { (Users.email eq email) and (Users.password eq password) }
                .map { ExposedUserToken(it[Users.accessToken], it[Users.tokenTime]) }
                .singleOrNull()
        }
    }

    override suspend fun updateUserByEmail(email: String, user: ExposedUser) {
        dbQuery {
            Users.update({ Users.email eq email }) {
                it[name] = user.name
                it[password] = user.password
                it[accessToken] = user.email
            }
        }
    }

    override suspend fun updateTokenByEmail(exposedUserToken: ExposedUserToken, email: String) {
        dbQuery {
            Users.update({ Users.email eq email }) {
                it[accessToken] = exposedUserToken.accessToken
                it[tokenTime] = exposedUserToken.tokenTime
            }
        }
    }

    override suspend fun deleteUserByEmail(email: String) {
        dbQuery {
            Users.deleteWhere { Users.email eq email }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

@Serializable
data class ExposedUser(
    val name: String,
    val email: String,
    val password: String,
)

@Serializable
data class ExposedUserToken(
    val accessToken: String,
    val tokenTime: Long,
)

@Serializable
data class ExposedUserLogin(
    val email: String,
    val password: String,
)

interface UserServiceInterface {

    suspend fun createNewUser(user: ExposedUser, token: String, time: Long): String

    suspend fun readUserByEmail(email: String): ExposedUser?

    suspend fun readAllUsers(): List<ExposedUser>

    suspend fun checkUserByToken(accessToken: String): ExposedUserToken?

    suspend fun checkUserByEmail(email: String): ExposedUserToken?

    suspend fun checkUserByEmailAndPassword(email: String, password: String): ExposedUserToken?

    suspend fun updateUserByEmail(email: String, user: ExposedUser)

    suspend fun updateTokenByEmail(exposedUserToken: ExposedUserToken, email: String)

    suspend fun deleteUserByEmail(email: String)
}

