package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object UserEdibles : Table("userEdibles") {
    val userId = integer("userId").references(Users.id)
    val edibleId = integer("edibleId").references(Edibles.id)
}