package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object UserEdibles : Table("userEdibles") {
    val userId = integer("userid").references(Users.id)
    val edibleId = integer("edibleid").references(Edibles.id)
    val grams = decimal("grams", 10, 2)
}