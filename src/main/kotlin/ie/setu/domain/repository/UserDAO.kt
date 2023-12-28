package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Manages the database transactions and returns the results of the transactions
 */
class UserDAO {
    /**
     * Retrieves all [user] in the User table.
     * @return list of all users.
     */
    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it))
            }
        }
        return userList
    }

    /**
     * Retrieves a [user] from the User table with specified id.
     * @return [user] with particular id.
     */
    fun findById(id: Int): User? {
        return transaction {
            Users.select() {
                Users.id eq id
            }
                    .map { mapToUser(it) }
                    .firstOrNull()
        }
    }

    /**
     * Retrieves a [user] from the User table with specified email.
     * @return [user] with particular email.
     */
    fun findByEmail(email: String): User? {
        return transaction {
            Users.select() { Users.email eq email }
                    .map { mapToUser(it) }
                    .firstOrNull()
        }
    }

    /**
     * Adds a [user] to the User table.
     * @return the id of the user following the add.
     */
    fun save(user: User): Int {
        return transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
            } get Users.id
        }
    }

    /**
     * Removes a [user] from the User table.
     * @return an int of transaction.
     */
    fun delete(id: Int): Int {
        return transaction {
            Users.deleteWhere {
                Users.id eq id
            }
        }
    }

    /**
     * Updates a [user] from the User table with specified fields.
     * @return an int of transaction.
     */
    fun update(id: Int, user: User): Int {
        return transaction {
            Users.update({
                Users.id eq id
            }) {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }
}
