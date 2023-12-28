package ie.setu.repository

import ie.setu.domain.UserEdible
import ie.setu.domain.db.Edibles
import ie.setu.domain.db.UserEdibles
import ie.setu.domain.db.Users
import ie.setu.domain.repository.EdibleDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.UserEdibleDAO
import ie.setu.helpers.userEdibles
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val userEdible1 = userEdibles[0]
val userEdible2 = userEdibles[1]

class UserEdibleDAOTest {
    @Nested
    inner class CreateUserEdibles {
        @Test
        fun `multiple user edibles added to table can be retrieved successfully`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
                Assertions.assertEquals(userEdible1, userEdibleDAO.findByUserId(userEdible1.userId)[0])
                Assertions.assertEquals(userEdible2, userEdibleDAO.findByUserId(userEdible2.userId)[0])
            }
        }
    }

    @Nested
    inner class ReadUserEdibles {
        @Test
        fun `getting all user edibles from a populated table returns all rows`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
            }
        }

        @Test
        fun `get user edible by user id that doesn't exist, results in no user returned`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(0, userEdibleDAO.findByUserId(-1).size)
            }
        }

        @Test
        fun `get user edible by user id that exists, results in a correct edible returned`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(userEdible1, userEdibleDAO.findByUserId(1)[0])
            }
        }

        @Test
        fun `get all user edibles over empty table returns none`() {
            transaction {
                SchemaUtils.create(UserEdibles)
                val userEdibleDAO = UserEdibleDAO()

                Assertions.assertEquals(0, userEdibleDAO.getAll().size)
            }
        }

        @Test
        fun `get user edible by edible id that doesn't exist, results in no user returned`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(0, userEdibleDAO.findByEdibleId(-1).size)
            }
        }

        @Test
        fun `get user edible by edible id that exists, results in a correct edible returned`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(userEdible1, userEdibleDAO.findByEdibleId(1)[0])
            }
        }
    }

    @Nested
    inner class UpdateUserEdibles {
        @Test
        fun `updating existing user edible, by user id, in table results in successful update`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                val userEdible2Updated = UserEdible(2, 2, 1000.0)
                userEdibleDAO.updateByUserId(userEdible2.userId, userEdible2Updated)
                Assertions.assertEquals(userEdible2Updated, userEdibleDAO.findByUserId(2)[0])
            }
        }

        @Test
        fun `updating non-existant user edible, by user id, in table results in no updates`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                val userEdible2Updated = UserEdible(2, 2, 1000.0)
                userEdibleDAO.updateByUserId(4, userEdible2Updated)
                Assertions.assertEquals(0, userEdibleDAO.findByUserId(4).size)
                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
            }
        }

        @Test
        fun `updating existing user edible, by edible id, in table results in successful update`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                val userEdible2Updated = UserEdible(2, 2, 1000.0)
                userEdibleDAO.updateByEdibleId(userEdible2.edibleId, userEdible2Updated)
                Assertions.assertEquals(userEdible2Updated, userEdibleDAO.findByEdibleId(2)[0])
            }
        }

        @Test
        fun `updating non-existant user edible, by edible id, in table results in no updates`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                val userEdible2Updated = UserEdible(2, 2, 1000.0)
                userEdibleDAO.updateByEdibleId(4, userEdible2Updated)
                Assertions.assertEquals(0, userEdibleDAO.findByEdibleId(4).size)
                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteUserEdibles {
        @Test
        fun `deleting a non-existant user edible by user id in table results in no deletion`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
                userEdibleDAO.deleteByUserId(4)
                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing user edible by user id in table results in record being deleted`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
                userEdibleDAO.deleteByUserId(userEdible2.userId)
                Assertions.assertEquals(1, userEdibleDAO.getAll().size)
            }
        }

        @Test
        fun `deleting a non-existant user edible by edible id in table results in no deletion`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
                userEdibleDAO.deleteByEdibleId(4)
                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing user edible by edible id in table results in record being deleted`() {
            transaction {
                val userEdibleDAO = populateUserEdibleTable()

                Assertions.assertEquals(2, userEdibleDAO.getAll().size)
                userEdibleDAO.deleteByEdibleId(userEdible2.edibleId)
                Assertions.assertEquals(1, userEdibleDAO.getAll().size)
            }
        }
    }

    internal fun populateUserEdibleTable(): UserEdibleDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(user1)
        userDAO.save(user2)

        SchemaUtils.create(Edibles)
        val edibleDAO = EdibleDAO()
        edibleDAO.save(edible1)
        edibleDAO.save(edible2)

        SchemaUtils.create(UserEdibles)
        val userEdibleDAO = UserEdibleDAO()
        userEdibleDAO.save(userEdible1)
        userEdibleDAO.save(userEdible2)
        return userEdibleDAO
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