package ie.setu.domain.repository

import ie.setu.domain.UserEdible
import ie.setu.domain.db.UserEdibles
import ie.setu.utils.mapToUserEdible
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserEdibleDAO {
    fun getAll(): ArrayList<UserEdible> {
        val userEdibleList: ArrayList<UserEdible> = arrayListOf()

        transaction {
            UserEdibles.selectAll().map { mapToUserEdible(it) }
        }

        return userEdibleList
    }

    fun findByUserId(userId: Int): UserEdible? {
        return transaction {
            UserEdibles.select() {
                UserEdibles.userId eq userId
            }
                    .map { mapToUserEdible(it) }
                    .firstOrNull()
        }
    }

    fun findByEdibleId(edibleId: Int): UserEdible? {
        return transaction {
            UserEdibles.select() {
                UserEdibles.edibleId eq edibleId
            }
                    .map { mapToUserEdible(it) }
                    .firstOrNull()
        }
    }

    fun save(userEdible: UserEdible): Int {
        return transaction {
            UserEdibles.insert {
                it[userId] = userEdible.userId
                it[edibleId] = userEdible.edibleId
            } get UserEdibles.userId
        }
    }

    fun updateByUserId(userId: Int, userEdible: UserEdible): Int {
        return transaction {
            UserEdibles.update({ UserEdibles.userId eq userId })
            {
                it[UserEdibles.userId] = userEdible.userId
                it[UserEdibles.edibleId] = userEdible.edibleId
            }
        }
    }

    fun updateByEdibleId(edibleId: Int, userEdible: UserEdible): Int {
        return transaction {
            UserEdibles.update({ UserEdibles.edibleId eq edibleId })
            {
                it[UserEdibles.userId] = userEdible.userId
                it[UserEdibles.edibleId] = userEdible.edibleId
            }
        }
    }

    fun deleteByUserId(userId: Int): Int {
        return transaction { UserEdibles.deleteWhere { UserEdibles.userId eq userId } }
    }

    fun deleteByEdibleId(edibleId: Int): Int {
        return transaction { UserEdibles.deleteWhere { UserEdibles.edibleId eq edibleId } }
    }
}