package ie.setu.controllers

import ie.setu.helpers.ServerContainer
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.jetbrains.exposed.sql.Database

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerControllerTestMockDB {
    //Don't connect to the production database; we will try mock this.
    // private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    //helper function to retrieve a test user from the database by id
    private fun retrieveUserById(id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }
}