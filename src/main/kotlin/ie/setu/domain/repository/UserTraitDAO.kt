package ie.setu.domain.repository

import ie.setu.domain.UserTrait
import ie.setu.domain.db.UserTraits
import ie.setu.utils.mapToUserTrait
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserTraitDAO {
    fun getAll(): ArrayList<UserTrait> {
        val userTraitList: ArrayList<UserTrait> = arrayListOf()

        transaction {
            UserTraits.selectAll().map {
                userTraitList.add(mapToUserTrait(it))
            }
        }

        return userTraitList
    }

    fun findByUserId(userId: Int): UserTrait? {
        return transaction {
            UserTraits.select() {
                UserTraits.userId eq userId
            }
                    .map { mapToUserTrait(it) }
                    .firstOrNull()
        }
    }

    fun save(userTrait: UserTrait): Int {
        return transaction {
            UserTraits.insert {
                it[userId] = userTrait.userId
                it[gender] = userTrait.gender
                it[weight] = userTrait.weight.toBigDecimal()
                it[height] = userTrait.height.toBigDecimal()
            } get UserTraits.userId
        }
    }

    fun delete(userId: Int): Int {
        return transaction { UserTraits.deleteWhere { UserTraits.userId eq userId } }
    }

    fun update(userId: Int, userTrait: UserTrait): Int {
        return transaction {
            UserTraits.update({ UserTraits.userId eq userId }) {
                it[UserTraits.userId] = userTrait.userId
                it[gender] = userTrait.gender
                it[weight] = userTrait.weight.toBigDecimal()
                it[height] = userTrait.height.toBigDecimal()
            }
        }
    }
}