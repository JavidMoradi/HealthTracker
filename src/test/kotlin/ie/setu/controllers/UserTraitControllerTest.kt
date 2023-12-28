package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.UserTrait
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTraitControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateUserTraits {
        @Test
        fun `add user trait with correct details returns a 201 response`() {
            val addResponse = addUserTrait(validUserId, validGender, validHeight, validWeight)
            Assertions.assertEquals(201, addResponse.status)

            val retrieved: UserTrait = jsonToObject(addResponse.body.toString())
            Assertions.assertEquals(validUserId, retrieved.userId)

            val deleteResponse = deleteUserTrait(retrieved.userId)
            Assertions.assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class ReadUserTraits {
        @Test
        fun `get all user traits from the database returns 200 or 404 response`() {
            val response = retrieveAllUserTraits()
            if (response.status == 200) {
                val ret: ArrayList<UserTrait> = jsonToObject(response.body.toString())
                Assertions.assertNotEquals(0, ret.size)
            } else {
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user trait when edible does not exist returns 404 response`() {
            val id = Integer.MIN_VALUE

            val retrieveResponse = retrieveUserTraitById(id)

            Assertions.assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting user trait when id exists, returns a 200 response`() {
            val addResponse = addUserTrait(validUserId, validGender, validHeight, validWeight)
            val added: UserTrait = jsonToObject(addResponse.body.toString())

            val retrieveResponse = retrieveUserTraitById(added.userId)
            Assertions.assertEquals(200, retrieveResponse.status)

            deleteUserTrait(added.userId)
        }
    }

    @Nested
    inner class UpdateUserTraits {
        @Test
        fun `updating user trait when it exists, returns a 204 response`() {
            val addResponse = addUserTrait(validUserId, validGender, validHeight, validWeight)
            val added: UserTrait = jsonToObject(addResponse.body.toString())

            Assertions.assertEquals(204, updateUserTrait(added.userId, updatedGender, validHeight, validWeight).status)

            val updatedResponse = retrieveUserTraitById(added.userId)
            val update: UserTrait = jsonToObject(updatedResponse.body.toString())
            Assertions.assertEquals(updatedGender, update.gender)

            deleteUserTrait(added.userId)
        }

        @Test
        fun `updating user trait when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, updateUserTrait(-1, updatedGender, validHeight, validWeight).status)
        }
    }

    @Nested
    inner class DeleteUserTraits {
        @Test
        fun `deleting user trait when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, deleteUserTrait(-1).status)
        }

        @Test
        fun `deleting user trait when it exists, returns a 204 response`() {
            val addResponse = addUserTrait(validUserId, validGender, validHeight, validWeight)
            val added: UserTrait = jsonToObject(addResponse.body.toString())

            Assertions.assertEquals(204, deleteUserTrait(added.userId).status)

            Assertions.assertEquals(404, retrieveUserTraitById(added.userId).status)
        }
    }

    // HELPER FUNCTIONS
    private fun retrieveAllUserTraits(): HttpResponse<String> {
        return Unirest.get("$origin/api/userTrait").asString()
    }

    private fun retrieveUserTraitById(id: Int): HttpResponse<String> {
        return Unirest.get("$origin/api/userTrait/$id").asString()
    }

    private fun addUserTrait(userId: Int, gender: String, height: Double, weight: Double): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/userTrait")
                .body(
                        "{\"userId\": \"$userId\",\"gender\": \"$gender\", \"height\": \"$height\", \"weight\": \"$weight\"}"
                )
                .asJson()
    }

    private fun updateUserTrait(userId: Int, gender: String, height: Double, weight: Double): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/userTrait/$userId")
                .body(
                        "{\"userId\": \"$userId\",\"gender\": \"$gender\", \"height\": \"$height\", \"weight\": \"$weight\"}"
                )
                .asJson()
    }

    private fun deleteUserTrait(userId: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/userTrait/$userId").asString()
    }
}