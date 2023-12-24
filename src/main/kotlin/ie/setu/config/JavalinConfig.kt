package ie.setu.config

import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

class JavalinConfig {
    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.staticFiles.enableWebjars()
            it.vue.vueAppName = "app" // only required for Vue 3, is defined in layout.html
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 : Not Found") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside the VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))

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
                path("userTrait") {
                    get(UserTraitController::getAll)
                    post(UserTraitController::addUserTrait)
                    path("{user-id}") {
                        get(UserTraitController::findByUserId)
                        delete(UserTraitController::deleteUserTrait)
                        patch(UserTraitController::updateUserTrait)
                    }
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

            path("/api/edibles") {
                get(EdibleController::getAllEdibles)
                post(EdibleController::addEdible)
                path("edible-id") {
                    get(EdibleController::getEdibleById)
                    delete(EdibleController::deleteEdible)
                    patch(EdibleController::updateEdible)
                }
            }

            path("api/userEdible") {
                get(UserEdibleController::getAll)
                post(UserEdibleController::addUserEdible)
                path("userEdible-userid") {
                    get(UserEdibleController::getUserEdibleByUserId)
                    patch(UserEdibleController::updateUserEdibleByUserId)
                    delete(UserEdibleController::deleteUserEdibleByUserId)
                }
                path("userEdible-edibleid") {
                    get(UserEdibleController::getUserEdibleByEdibleId)
                    patch(UserEdibleController::updateUserEdibleByEdibleId)
                    delete(UserEdibleController::deleteUserEdibleByEdibleId)
                }
            }
        }
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7000
    }
}
