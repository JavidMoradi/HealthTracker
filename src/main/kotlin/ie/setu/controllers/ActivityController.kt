package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object ActivityController {
    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

    fun getAllActivities(ctx: Context) {
        // mapper handles the deserialization of Joda date into a String.
        val mapper = jsonObjectMapper()
        val activites = activityDAO.getAll()
        if (activites.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(mapper.writeValueAsString(activites))
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                ctx.status(200)
                // mapper handles the deserialization of Joda date into a String.
                val mapper = jsonObjectMapper()
                ctx.json(mapper.writeValueAsString(activities))
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    fun getActivityByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())
        if (activity != null) {
            ctx.status(200)
            val mapper = jsonObjectMapper()
            ctx.json(mapper.writeValueAsString(activity))
        } else {
            ctx.status(404)
        }
    }

    fun addActivity(ctx: Context) {
        // mapper handles the serialisation of Joda date into a String.
        val mapper = jsonObjectMapper()
        val activity = mapper.readValue<Activity>(ctx.body())
        val activityId = activityDAO.save(activity)
        if (activityId != null) {
            activity.id = activityId
            ctx.status(201)
            // ctx.json(activity)
        }
    }

    fun updateActivity(ctx: Context) {
        val activity: Activity = jsonToObject(ctx.body())
        if (activityDAO.update(id = ctx.pathParam("activity-id").toInt(), activity = activity) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteActivity(ctx: Context) {
        if (activityDAO.delete(ctx.pathParam("activity-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteActivityByUserId(ctx: Context) {
        if (activityDAO.deleteAll(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}