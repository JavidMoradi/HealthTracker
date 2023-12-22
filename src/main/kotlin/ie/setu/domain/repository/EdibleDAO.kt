package ie.setu.domain.repository

import ie.setu.domain.Edible
import ie.setu.domain.db.Edibles
import ie.setu.utils.mapToEdible
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class EdibleDAO {
    fun getAll(): ArrayList<Edible> {
        val edibleList = arrayListOf<Edible>()
        transaction {
            Edibles.selectAll().map {
                edibleList.add(mapToEdible(it))
            }
        }
        return edibleList
    }

    fun findById(id: Int): Edible? {
        return transaction {
            Edibles.select() {
                Edibles.id eq id
            }
                    .map { mapToEdible(it) }
                    .firstOrNull()
        }
    }

    fun save(edible: Edible): Int {
        return transaction {
            Edibles.insert {
                it[calories] = edible.calories
                it[protein] = edible.protein.toBigDecimal()
                it[carb] = edible.carb.toBigDecimal()
                it[fat] = edible.fat.toBigDecimal()
                it[fiber] = edible.fiber.toBigDecimal()
                it[sugar] = edible.sugar.toBigDecimal()
            } get Edibles.id
        }
    }

    fun delete(id: Int): Int {
        return transaction {
            Edibles.deleteWhere { Edibles.id eq id }
        }
    }

    fun update(id: Int, edible: Edible): Int {
        return transaction {
            Edibles.update({ Edibles.id eq id }) {
                it[calories] = edible.calories
                it[protein] = edible.protein.toBigDecimal()
                it[carb] = edible.carb.toBigDecimal()
                it[fat] = edible.fat.toBigDecimal()
                it[fiber] = edible.fiber.toBigDecimal()
                it[sugar] = edible.sugar.toBigDecimal()
            }
        }
    }
}