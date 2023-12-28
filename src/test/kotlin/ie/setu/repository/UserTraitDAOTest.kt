package ie.setu.repository

import ie.setu.domain.UserTrait
import ie.setu.domain.db.UserTraits
import ie.setu.domain.db.Users
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.UserTraitDAO
import ie.setu.helpers.userTraits
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val userTrait1 = userTraits[0]
val userTrait2 = userTraits[1]
val userTrait3 = userTraits[2]

class UserTraitDAOTest {
    @Nested
    inner class CreateUserTraits {
        @Test
        fun `multiple user traits added to table can be retrieved successfully`() {
            transaction {
                val userTraitDAO = populateUserTraitTable()

                Assertions.assertEquals(3, userTraitDAO.getAll().size)
                Assertions.assertEquals(userTrait1, userTraitDAO.findByUserId(userTrait1.userId))
                Assertions.assertEquals(userTrait2, userTraitDAO.findByUserId(userTrait2.userId))
                Assertions.assertEquals(userTrait3, userTraitDAO.findByUserId(userTrait3.userId))
            }
        }
    }

    @Nested
    inner class ReadUserTraits {
        @Test
        fun `getting all user traits from a populated table returns all rows`() {
            transaction {
                val userTraitDAO = populateUserTraitTable()

                Assertions.assertEquals(3, userTraitDAO.getAll().size)
            }
        }

        @Test
        fun `get user trait by user id that doesn't exist, results in no user returned`() {
            transaction {
                val userTraitDAO = populateUserTraitTable()

                Assertions.assertEquals(null, userTraitDAO.findByUserId(-1))
            }
        }

        @Test
        fun `get user trait by user id that exists, results in a correct edible returned`() {
            transaction {
                val userTraitDAO = populateUserTraitTable()

                Assertions.assertEquals(userTrait1, userTraitDAO.findByUserId(1))
            }
        }

        @Test
        fun `get all user traits over empty table returns none`() {
            transaction {
                SchemaUtils.create(UserTraits)
                val userTraitDAO = UserTraitDAO()

                Assertions.assertEquals(0, userTraitDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateUserTraits {
        @Test
        fun `updating existing user trait in table results in successful update`() {
            transaction {
                val userTraitDAO = populateUserTraitTable()

                val userTrait3Updated = UserTrait(3, "male", 10.0, 10.0)
                userTraitDAO.update(userTrait3.userId, userTrait3Updated)
                Assertions.assertEquals(userTrait3Updated, userTraitDAO.findByUserId(3))
            }
        }

        @Test
        fun `updating non-existant user trait in table results in no updates`() {
            transaction {
                val userTraitDAO = populateUserTraitTable()

                val userTrait4Updated = UserTrait(4, "male", 10.0, 10.0)
                userTraitDAO.update(4, userTrait4Updated)
                Assertions.assertEquals(null, userTraitDAO.findByUserId(4))
                Assertions.assertEquals(3, userTraitDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteUserTraits {
        @Test
        fun `deleting a non-existant user trait in table results in no deletion`() {
            transaction {
                val userTraitDAO = populateUserTraitTable()

                Assertions.assertEquals(3, userTraitDAO.getAll().size)
                userTraitDAO.delete(4)
                Assertions.assertEquals(3, userTraitDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing user trait in table results in record being deleted`() {
            transaction {
                val userTraitDAO = populateUserTraitTable()

                Assertions.assertEquals(3, userTraitDAO.getAll().size)
                userTraitDAO.delete(userTrait3.userId)
                Assertions.assertEquals(2, userTraitDAO.getAll().size)
            }
        }
    }

    internal fun populateUserTraitTable(): UserTraitDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)

        SchemaUtils.create(UserTraits)
        val userTraitDAO = UserTraitDAO()
        userTraitDAO.save(userTrait1)
        userTraitDAO.save(userTrait2)
        userTraitDAO.save(userTrait3)

        return userTraitDAO
    }

    companion object {
        // Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }
}