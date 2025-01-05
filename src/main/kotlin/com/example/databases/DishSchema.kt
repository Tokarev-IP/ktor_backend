package com.example.databases

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ExposedDish(
    val id: String,
    val name: String,
    val price: Int,
    val description: String
)

class DishService(database: Database) {
    object Dishes : Table() {
        val id = varchar("id", length = 50).uniqueIndex()
        val name = varchar("name", length = 50)
        val price = integer("price")
        val description = varchar("description", length = 250)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Dishes)
        }
    }

    suspend fun create(dish: ExposedDish): String = dbQuery {
        Dishes.insert {
            it[id] = dish.id
            it[name] = dish.name
            it[price] = dish.price
            it[description] = dish.description
        }[Dishes.id]
    }

    suspend fun read(id: String): ExposedDish? {
        return dbQuery {
            Dishes.selectAll()
                .where { Dishes.id eq id }
                .map { ExposedDish(it[Dishes.id], it[Dishes.name], it[Dishes.price], it[Dishes.description]) }
                .singleOrNull()
        }
    }

    suspend fun readAll(): List<ExposedDish> {
        return dbQuery {
            Dishes.selectAll()
                .map { ExposedDish(it[Dishes.id], it[Dishes.name], it[Dishes.price], it[Dishes.description]) }
        }
    }

    suspend fun update(id: String, dish: ExposedDish) {
        dbQuery {
            Dishes.update({ Dishes.id eq id }) {
                it[name] = dish.name
                it[price] = dish.price
                it[description] = dish.description
            }
        }
    }

    suspend fun delete(id: String) {
        dbQuery {
            Dishes.deleteWhere { Dishes.id.eq(id) }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}