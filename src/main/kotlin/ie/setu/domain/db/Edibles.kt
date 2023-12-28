package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object Edibles : Table("edibles") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 255)
    val calories = integer("calories")
    val protein = decimal("protein", 10, 2)
    val carb = decimal("carb", 10, 2)
    val fat = decimal("fat", 10, 2)
    val fiber = decimal("fiber", 10, 2)
    val sugar = decimal("sugar", 10, 2)
}