package ie.setu.config

import com.fasterxml.jackson.module.kotlin.jsonMapper
import ie.setu.controllers.ActivityController
import ie.setu.controllers.HealthTrackerController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson

class JavalinConfig
{
    fun startJavalinService(): Javalin
    {
        val app = Javalin.create {
            //add this jsonMapper to serialise objects to json
            it.jsonMapper(JavalinJackson(jsonMapper()))
        }
            .apply {
                exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
                error(404) { ctx -> ctx.json("404 - Not Found") }
            }
            .start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin)
    {
        app.routes {
            path("/api/users") {
                get(HealthTrackerController::getAllUsers)
                post(HealthTrackerController::addUser)
                path("{user-id}") {
                    get(HealthTrackerController::getUserByUserId)
                    delete(HealthTrackerController::deleteUser)
                    patch(HealthTrackerController::updateUser)
                    path("activities") {
                        get(ActivityController::getActivitiesByUserId)
                    }
                }
                path("email/{email}") {
                    get(HealthTrackerController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
            }
        }
    }

    private fun getRemoteAssignedPort(): Int
    {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null)
        {
            Integer.parseInt(remotePort)
        }
        else 7000
    }
}
