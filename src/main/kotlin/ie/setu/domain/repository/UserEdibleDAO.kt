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
            UserEdibles.selectAll().map { userEdibleList.add(mapToUserEdible(it)) }
        }

        return userEdibleList
    }

    fun findByUserId(userId: Int): ArrayList<UserEdible> {
        val userEdibleList: ArrayList<UserEdible> = arrayListOf()

        transaction {
            UserEdibles.select() {
                UserEdibles.userId eq userId
            }
                    .map { userEdibleList.add(mapToUserEdible(it)) }
        }

        return userEdibleList
    }

    fun findByEdibleId(edibleId: Int): ArrayList<UserEdible> {
        val userEdibleList: ArrayList<UserEdible> = arrayListOf()

        transaction {
            UserEdibles.select() {
                UserEdibles.edibleId eq edibleId
            }
                    .map { userEdibleList.add(mapToUserEdible(it)) }
        }

        return userEdibleList
    }

    fun save(userEdible: UserEdible): Int {
        return transaction {
            UserEdibles.insert {
                it[userId] = userEdible.userId
                it[edibleId] = userEdible.edibleId
                it[grams] = userEdible.grams.toBigDecimal()
            } get UserEdibles.userId
        }
    }

    fun updateByUserId(userId: Int, userEdible: UserEdible): Int {
        return transaction {
            UserEdibles.update({ UserEdibles.userId eq userId })
            {
                it[UserEdibles.userId] = userEdible.userId
                it[UserEdibles.edibleId] = userEdible.edibleId
                it[grams] = userEdible.grams.toBigDecimal()
            }
        }
    }

    fun updateByEdibleId(edibleId: Int, userEdible: UserEdible): Int {
        return transaction {
            UserEdibles.update({ UserEdibles.edibleId eq edibleId })
            {
                it[UserEdibles.userId] = userEdible.userId
                it[UserEdibles.edibleId] = userEdible.edibleId
                it[grams] = userEdible.grams.toBigDecimal()
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