package ie.setu.repository

import ie.setu.domain.Edible
import ie.setu.domain.db.Edibles
import ie.setu.domain.repository.EdibleDAO
import ie.setu.helpers.edibles
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val edible1 = edibles[0]
val edible2 = edibles[1]
val edible3 = edibles[2]

class EdibleDAOTest {
    @Nested
    inner class CreateEdibles {
        @Test
        fun `multiple edibles added to table can be retrieved successfully`() {
            transaction {
                val edibleDAO = populateEdibleTable()

                Assertions.assertEquals(3, edibleDAO.getAll().size)
                Assertions.assertEquals(edible1, edibleDAO.findById(edible1.id))
                Assertions.assertEquals(edible2, edibleDAO.findById(edible2.id))
                Assertions.assertEquals(edible3, edibleDAO.findById(edible3.id))
            }
        }
    }

    @Nested
    inner class ReadEdibles {
        @Test
        fun `getting all edibles from a populated table returns all rows`() {
            transaction {
                val edibleDAO = populateEdibleTable()

                Assertions.assertEquals(3, edibleDAO.getAll().size)
            }
        }

        @Test
        fun `get edible by id that doesn't exist, results in no user returned`() {
            transaction {
                val edibleDAO = populateEdibleTable()

                Assertions.assertEquals(null, edibleDAO.findById(-1))
            }
        }

        @Test
        fun `get edible by id that exists, results in a correct edible returned`() {
            transaction {
                val edibleDAO = populateEdibleTable()

                Assertions.assertEquals(edible1, edibleDAO.findById(1))
            }
        }

        @Test
        fun `get all edibles over empty table returns none`() {
            transaction {
                SchemaUtils.create(Edibles)
                val edibleDAO = EdibleDAO()

                Assertions.assertEquals(0, edibleDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateEdibles {
        @Test
        fun `updating existing edible in table results in successful update`() {
            transaction {
                val edibleDAO = populateEdibleTable()

                val edible3Updated = Edible(3, "new name", 1000, 1.1, 1.1, 1.1, 1.1, 1.1)
                edibleDAO.update(edible3.id, edible3Updated)
                Assertions.assertEquals(edible3Updated, edibleDAO.findById(3))
            }
        }

        @Test
        fun `updating non-existant edible in table results in no updates`() {
            transaction {
                val edibleDAO = populateEdibleTable()

                val edible4Updated = Edible(4, "new name", 1000, 1.1, 1.1, 1.1, 1.1, 1.1)
                edibleDAO.update(4, edible4Updated)
                Assertions.assertEquals(null, edibleDAO.findById(4))
                Assertions.assertEquals(3, edibleDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteEdibles {
        @Test
        fun `deleting a non-existant edible in table results in no deletion`() {
            transaction {
                val edibleDAO = populateEdibleTable()

                Assertions.assertEquals(3, edibleDAO.getAll().size)
                edibleDAO.delete(4)
                Assertions.assertEquals(3, edibleDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing edible in table results in record being deleted`() {
            transaction {
                val edibleDAO = populateEdibleTable()

                Assertions.assertEquals(3, edibleDAO.getAll().size)
                edibleDAO.delete(edible3.id)
                Assertions.assertEquals(2, edibleDAO.getAll().size)
            }
        }
    }

    internal fun populateEdibleTable(): EdibleDAO {
        SchemaUtils.create(Edibles)
        val edibleDAO = EdibleDAO()
        edibleDAO.save(edible1)
        edibleDAO.save(edible2)
        edibleDAO.save(edible3)
        return edibleDAO
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