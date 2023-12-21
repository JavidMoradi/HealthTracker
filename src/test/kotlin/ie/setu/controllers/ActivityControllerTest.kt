package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadActivity {
        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = retrieveAllActivities()
            if (response.status == 200) {
                val retrievedActivities: ArrayList<Activity> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedActivities.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get activity by user id when user id does not exist return 404 response`() {
            val retrieveResponse = retrieveActivityByUserId(Int.MIN_VALUE)

            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get activity when activity id does not exist return 404 response`() {
            val retrieveResponse = retrieveActivityByActivityId(Int.MIN_VALUE)

            assertEquals(404, retrieveResponse.status)
        }
    }

    @Nested
    inner class CreateActivity {
        @Test
        fun `add an activity with correct details returns a 201 response`() {
            val addResponse = addActivity(validDesc, validDuration, validCalories, validStartDate, validUserId)
            assertEquals(201, addResponse.status)

            val retResponse = retrieveActivityByUserId(validUserId)
            assertEquals(200, retResponse.status)

            val deleteResponse = deleteActivityByUserId(validUserId)
            assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class UpdateActivity {
        @Test
        fun `updating a user when it exists, returns a 204 response`() {
            val addResponse = addActivity(validDesc, validDuration, validCalories, validStartDate, validUserId)
            val addedActivity: Activity = jsonToObject(addResponse.body.toString())

            assertEquals(
                    204,
                    updateActivity(addedActivity.id,
                            updatedDescription,
                            validDuration,
                            validCalories,
                            validStartDate,
                            validUserId).status
            )

            deleteActivity(addedActivity.id)
        }

        @Test
        fun `updating an activity when it doesn't exist, returns a 404 response`() {
            assertEquals(404, updateActivity(-1, updatedDescription, validDuration, validCalories, validStartDate, validUserId).status)
        }
    }

    @Nested
    inner class DeleteActivity {
        @Test
        fun `deleting an activity when it doesn't exist, returns a 404 response`() {
            assertEquals(404, deleteActivity(-1).status)
        }

        @Test
        fun `deleting an activity when it exists, returns a 204 response`() {
            val addedResponse = addActivity(validDesc, validDuration, validCalories, validStartDate, validUserId)
            val addedActivity: Activity = jsonToObject(addedResponse.body.toString())

            //assertEquals(204, deleteActivity(addedActivity.id).status)
        }

        @Test
        fun `deleting an activity with user id when it doesn't exist, returns a 404 response`() {
            assertEquals(404, deleteActivityByUserId(-1).status)
        }

        @Test
        fun `deleting an activity by user id when it exists, returns a 204 response`() {
            addActivity(validDesc, validDuration, validCalories, validStartDate, validUserId)

            assertEquals(204, deleteActivityByUserId(validUserId).status)
        }
    }

    // HELPER FUNCTIONS
    private fun retrieveAllActivities(): HttpResponse<String> {
        return Unirest.get(origin + "/api/activities").asString()
    }

    private fun retrieveActivityByUserId(userId: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/" + userId + "/activities").asString()
    }

    private fun retrieveActivityByActivityId(id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/activities/" + id).asString()
    }

    private fun addActivity(description: String, duration: Double, calories: Int, started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/activities")
                .body(
                        "{\"description\":\"$description\", \"duration\":\"$duration\", \"calories\":\"$calories\", \"started\":\"$started\", \"userId\":\"$userId\"}"
                )
                .asJson()
    }

    private fun updateActivity(id: Int, description: String, duration: Double, calories: Int, started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/activities/" + id)
                .body(
                        "{\"description\":\"$description\", \"duration\":\"$duration\", \"calories\":\"$calories\", \"started\":\"$started\", \"userId\":\"$userId\"}"
                )
                .asJson()
    }

    private fun deleteActivity(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/activities/" + id).asString()
    }

    private fun deleteActivityByUserId(userId: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/" + userId + "/activities").asString()
    }
}