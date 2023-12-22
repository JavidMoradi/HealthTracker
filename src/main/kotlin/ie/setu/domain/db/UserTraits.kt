package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object UserTraits : Table("userTraits") {
    val userId = integer("userId").references(Users.id)
    val gender = varchar("gender", 255)
    val height = decimal("height", 10, 1)
    val weight = decimal("weight", 10, 1)
}