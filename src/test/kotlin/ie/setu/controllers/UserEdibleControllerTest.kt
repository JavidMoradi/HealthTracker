package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.UserEdible
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
class UserEdibleControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateUserEdibles {
        @Test
        fun `add user edible with correct details returns a 201 response`() {
            val addResponse = addUserEdible(validUserId, validEdibleId, validGrams.toInt())
            Assertions.assertEquals(201, addResponse.status)

            val retrievedUserEdible: UserEdible = jsonToObject(addResponse.body.toString())
            Assertions.assertEquals(validGrams, retrievedUserEdible.grams)

            val deleteResponse = deleteUserEdibleByEdibleId(retrievedUserEdible.edibleId)
            Assertions.assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class ReadUserEdibles {
        @Test
        fun `get all user edibles from the database returns 200 or 404 response`() {
            val response = retrieveAllUserEdibles()
            if (response.status == 200) {
                val retrievedUserEdibles: ArrayList<UserEdible> = jsonToObject(response.body.toString())
                Assertions.assertNotEquals(0, retrievedUserEdibles.size)
            } else {
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user edible by user id when id does not exist returns 404 response`() {
            val id = Integer.MIN_VALUE

            val retrieveResponse = retrieveUserEdibleByUserId(id)

            Assertions.assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting user edible by user id when id exists, returns a 200 response`() {
            val addResponse = addUserEdible(validUserId, validEdibleId, validGrams.toInt())
            val addeduserEdible: UserEdible = jsonToObject(addResponse.body.toString())

            val retrieveResponse = retrieveUserEdibleByUserId(addeduserEdible.userId)
            Assertions.assertEquals(200, retrieveResponse.status)

            deleteUserEdibleByEdibleId(addeduserEdible.edibleId)
        }

        @Test
        fun `get user edible by edible id when id does not exist returns 404 response`() {
            val id = Integer.MIN_VALUE

            val retrieveResponse = retrieveUserEdibleByEdibleId(id)

            Assertions.assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting user edible by edible id when id exists, returns a 200 response`() {
            val addResponse = addUserEdible(validUserId, validEdibleId, validGrams.toInt())
            val addeduserEdible: UserEdible = jsonToObject(addResponse.body.toString())

            val retrieveResponse = retrieveUserEdibleByEdibleId(addeduserEdible.userId)
            Assertions.assertEquals(200, retrieveResponse.status)

            deleteUserEdibleByEdibleId(addeduserEdible.edibleId)
        }
    }

    @Nested
    inner class UpdateUserEdibles {
        @Test
        fun `updating user edible by user id when it exists, returns a 204 response`() {
            val addResponse = addUserEdible(validUserId, validEdibleId, validGrams.toInt())
            val addeduserEdible: UserEdible = jsonToObject(addResponse.body.toString())

            Assertions.assertEquals(404, updateUserEdibleByUserId(addeduserEdible.userId, updateUserId, validEdibleId, validGrams.toInt()).status)

            deleteUserEdibleByEdibleId(addeduserEdible.edibleId)
        }

        @Test
        fun `updating user edible when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, updateUserEdibleByUserId(-1, -1, -1, validGrams.toInt()).status)
        }

        @Test
        fun `updating user edible by edible id when it exists, returns a 204 response`() {
            val addResponse = addUserEdible(validUserId, validEdibleId, validGrams.toInt())
            val addeduserEdible: UserEdible = jsonToObject(addResponse.body.toString())

            Assertions.assertEquals(404, updateUserEdibleByEdibleId(addeduserEdible.edibleId, validUserId, updatedEdibleId, validGrams.toInt()).status)

            deleteUserEdibleByEdibleId(addeduserEdible.edibleId)
        }
    }

    @Nested
    inner class DeleteUserEdibles {
        @Test
        fun `deleting user edible when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, deleteUserEdibleByUserId(-1).status)
        }

        @Test
        fun `deleting user edible bu edible id when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, deleteUserEdibleByEdibleId(-1).status)
        }

        @Test
        fun `deleting user edible when it exists, returns a 204 response`() {
            val addResponse = addUserEdible(validUserId, validEdibleId, validGrams.toInt())
            val addeduserEdible: UserEdible = jsonToObject(addResponse.body.toString())

            Assertions.assertEquals(204, deleteUserEdibleByEdibleId(addeduserEdible.edibleId).status)
        }

        @Test
        fun `deleting user edible bu user id when it exists, returns a 204 response`() {
            val addResponse = addUserEdible(validUserId, validEdibleId, validGrams.toInt())
            val addeduserEdible: UserEdible = jsonToObject(addResponse.body.toString())

            Assertions.assertEquals(204, deleteUserEdibleByUserId(addeduserEdible.userId).status)
        }
    }


    // HELPER FUNCTIONS
    private fun retrieveAllUserEdibles(): HttpResponse<String> {
        return Unirest.get("$origin/api/userEdible").asString()
    }

    private fun retrieveUserEdibleByUserId(userId: Int): HttpResponse<String> {
        return Unirest.get("$origin/api/userEdible/user/$userId").asString()
    }

    private fun retrieveUserEdibleByEdibleId(edibleId: Int): HttpResponse<String> {
        return Unirest.get("$origin/api/userEdible/edible/$edibleId").asString()
    }

    private fun addUserEdible(userId: Int, edibleId: Int, grams: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/userEdible")
                .body(
                        "{\"userId\": \"$userId\", \"edibleId\": \"$edibleId\", \"grams\": \"$grams\"}"
                ).asJson()
    }

    private fun updateUserEdibleByUserId(id: Int, userId: Int, edibleId: Int, grams: Int): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/userEdible/user/$userId")
                .body(
                        "{\"userId\": \"$userId\", \"edibleId\": \"$edibleId\", \"grams\": \"$grams\"}"
                ).asJson()
    }

    private fun updateUserEdibleByEdibleId(id: Int, userId: Int, edibleId: Int, grams: Int): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/userEdible/edible/$edibleId")
                .body(
                        "{\"userId\": \"$userId\", \"edibleId\": \"$edibleId\", \"grams\": \"$grams\"}"
                ).asJson()
    }

    private fun deleteUserEdibleByUserId(userId: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/userEdible/user/$userId").asString()
    }

    private fun deleteUserEdibleByEdibleId(edibleId: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/userEdible/edible/$edibleId").asString()
    }
}