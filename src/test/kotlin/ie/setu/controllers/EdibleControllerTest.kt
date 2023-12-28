package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Edible
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
class EdibleControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateEdibles {
        @Test
        fun `add an edible with correct details returns a 201 response`() {
            val addResponse = addEdible(validName, validCals, validProtein, validCarbs, validFat, validFiber, validSugar)
            Assertions.assertEquals(201, addResponse.status)

            val retrievedEdible: Edible = jsonToObject(addResponse.body.toString())
            Assertions.assertEquals(validCals, retrievedEdible.calories)
            Assertions.assertEquals(validName, retrievedEdible.name)

            val deleteResponse = deleteEdible(retrievedEdible.id)
            Assertions.assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class ReadEdibles {
        @Test
        fun `get all edibles from the database returns 200 or 404 response`() {
            val response = retrieveAllEdibles()
            if (response.status == 200) {
                val retrievedEdibles: ArrayList<Edible> = jsonToObject(response.body.toString())
                Assertions.assertNotEquals(0, retrievedEdibles.size)
            } else {
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get edible by id when edible does not exist returns 404 response`() {
            val id = Integer.MIN_VALUE

            val retrieveResponse = retrieveEdibleById(id)

            Assertions.assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting edible by id when id exists, returns a 200 response`() {
            val addResponse = addEdible(validName, validCals, validProtein, validCarbs, validFat, validFiber, validSugar)
            val addedEdible: Edible = jsonToObject(addResponse.body.toString())

            val retrieveResponse = retrieveEdibleById(addedEdible.id)
            Assertions.assertEquals(200, retrieveResponse.status)

            deleteEdible(addedEdible.id)
        }
    }

    @Nested
    inner class UpdateEdibles {
        @Test
        fun `updating edible when it exists, returns a 204 response`() {
            val addResponse = addEdible(validName, validCals, validProtein, validCarbs, validFat, validFiber, validSugar)
            val addedEdible: Edible = jsonToObject(addResponse.body.toString())

            Assertions.assertEquals(204, updateEdible(addedEdible.id, updatedName, validCals, validProtein, validCarbs, validFat, validFiber, validSugar).status)

            val updatedEdibleResponse = retrieveEdibleById(addedEdible.id)
            val updatedEdible: Edible = jsonToObject(updatedEdibleResponse.body.toString())
            Assertions.assertEquals(updatedName, updatedEdible.name)

            deleteEdible(addedEdible.id)
        }

        @Test
        fun `updating edible when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, updateEdible(-1, updatedName, validCals, validProtein, validCarbs, validFat, validFiber, validSugar).status)
        }
    }

    @Nested
    inner class DeleteEdibles {
        @Test
        fun `deleting edible when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, deleteEdible(-1).status)
        }

        @Test
        fun `deleting edible when it exists, returns a 204 response`() {
            val addResponse = addEdible(validName, validCals, validProtein, validCarbs, validFat, validFiber, validSugar)
            val addedEdible: Edible = jsonToObject(addResponse.body.toString())

            Assertions.assertEquals(204, deleteEdible(addedEdible.id).status)

            Assertions.assertEquals(404, retrieveEdibleById(addedEdible.id).status)
        }
    }

    // HELPER FUNCTIONS
    private fun retrieveAllEdibles(): HttpResponse<String> {
        return Unirest.get("$origin/api/edibles").asString()
    }

    private fun retrieveEdibleById(id: Int): HttpResponse<String> {
        return Unirest.get("$origin/api/edibles/$id").asString()
    }

    private fun addEdible(name: String, cal: Int,
                          protein: Double, carb: Double,
                          fat: Double, fiber: Double, sugar: Double): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/edibles").body(
                "{\"name\": \"$name\", \"calories\": \"$cal\", \"protein\": \"$protein\", \"carb\": \"$carb\", \"fat\": \"$fat\", \"fiber\": \"$fiber\", \"sugar\": \"$sugar\"}"
        )
                .asJson()
    }

    private fun deleteEdible(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/edibles/$id").asString()
    }

    private fun updateEdible(id: Int, name: String, cal: Int,
                             protein: Double, carb: Double,
                             fat: Double, fiber: Double, sugar: Double): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/edibles/$id").body(
                "{\"name\": \"$name\", \"calories\": \"$cal\", \"protein\": \"$protein\", \"carb\": \"$carb\", \"fat\": \"$fat\", \"fiber\": \"$fiber\", \"sugar\": \"$sugar\"}"
        )
                .asJson()
    }
}