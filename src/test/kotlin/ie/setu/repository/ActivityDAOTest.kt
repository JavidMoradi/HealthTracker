package ie.setu.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.activities
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val activity1 = activities[0]
val activity2 = activities[1]
val activity3 = activities[2]
val activity4 = activities[3]

class ActivityDAOTest {
    @Nested
    inner class CreateActivities {
        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {
                // Arrange - create and populate table with three users
                val activityDAO = populateActivityTable()

                // Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
                assertEquals(activity2, activityDAO.findByActivityId(activity2.id))
                assertEquals(activity3, activityDAO.findByActivityId(activity3.id))
            }
        }
    }

    @Nested
    inner class ReadActivities {
        @Test
        fun `get activity by activity id that exist results in a correct activity returned`() {
            transaction {
                val activityDAO = populateActivityTable()

                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
            }
        }

        @Test
        fun `get activity by activity id that does not exist results in no activity returned`() {
            transaction {
                val activityDAO = populateActivityTable()

                assertEquals(null, activityDAO.findByActivityId(-99))
            }
        }

        @Test
        fun `get activity by user id that exist results in a correct activity returned`() {
            transaction {
                val activityDAO = populateActivityTable()

                assertEquals(activity1, activityDAO.findByUserId(activity1.userId)[0])
            }
        }

        @Test
        fun `get activity by user id that does not exist results in no activity returned`() {
            transaction {
                val activityDAO = populateActivityTable()

                assertEquals(emptyList<Activity>(), activityDAO.findByUserId(-99))
            }
        }

        @Test
        fun `get all activities over empty table returns none`() {
            transaction {
                SchemaUtils.create(Activities)
                val activityDAO = ActivityDAO()

                assertEquals(0, activityDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateActivities {
        @Test
        fun `updating existing activity in table results in successful update`() {
            transaction {
                // Arrange - create and populate table with three users
                val activityDAO = populateActivityTable()

                // Act & Assert
                val activity1Updated = Activity(1, "update description", 10.5, 1000, DateTime.now(), 1)
                activityDAO.update(activity1.id, activity1Updated)
                assertEquals(activity1Updated, activityDAO.findByActivityId(1))
            }
        }

        @Test
        fun `updating non-existing activity in table results in no updates`() {
            transaction {
                //Arrange - create and populate table with three users
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activity9Updated = Activity(9, "update description", 10.5, 1000, DateTime.now(), 1)
                activityDAO.update(9, activity9Updated)
                assertEquals(null, activityDAO.findByActivityId(9))
                assertEquals(3, activityDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteActivities {
        @Test
        fun `deleting a non-existing activity in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with three users
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.delete(4)
                assertEquals(3, activityDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity in table results in record being deleted`() {
            transaction {
                //Arrange - create and populate table with three users
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.delete(activity1.id)
                assertEquals(2, activityDAO.getAll().size)
            }
        }

        @Test
        fun `deleting all activities of a user in table results in correct deletion`() {
            transaction {
                //Arrange - create and populate table with three users
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.deleteAll(user1.id)
                assertEquals(2, activityDAO.getAll().size)
            }
        }

        @Test
        fun `deleting all activities of a non-existing user in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with three users
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.deleteAll(-1)
                assertEquals(3, activityDAO.getAll().size)
            }
        }
    }

    internal fun populateActivityTable(): ActivityDAO {
        SchemaUtils.create(Activities)
        SchemaUtils.create(Users)

        val userDAO = UserDAO()
        val activityDAO = ActivityDAO()

        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)

        activityDAO.save(activity1)
        activityDAO.save(activity2)
        activityDAO.save(activity3)

        return activityDAO
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