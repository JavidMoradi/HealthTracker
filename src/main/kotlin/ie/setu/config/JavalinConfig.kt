package ie.setu.config

import com.fasterxml.jackson.module.kotlin.jsonMapper
import ie.setu.controllers.ActivityController
import ie.setu.controllers.UserController
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
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}") {
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("activities") {
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivityByUserId)
                    }
                }
                path("email/{email}") {
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}") {
                    get(ActivityController::getActivityByActivityId)
                    patch(ActivityController::updateActivity)
                    delete(ActivityController::deleteActivity)
                }
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
