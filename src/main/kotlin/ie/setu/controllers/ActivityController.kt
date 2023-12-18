package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import io.javalin.http.Context

object ActivityController
{
    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

    fun getAllActivities(ctx: Context)
    {
        // mapper handles the deserialization of Joda date into a String.
        val mapper = jsonObjectMapper()
        ctx.json(mapper.writeValueAsString(activityDAO.getAll()))
    }

    fun getActivitiesByUserId(ctx: Context)
    {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null)
        {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty())
            {
                // mapper handles the deserialization of Joda date into a String.
                val mapper = jsonObjectMapper()
                ctx.json(mapper.writeValueAsString(activities))
            }
        }
    }

    fun getActivityByActivityId(ctx: Context)
    {
        val activity = activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())
        if (activity != null)
        {
            val mapper = jsonObjectMapper()
            ctx.json(mapper.writeValueAsString(activity))
        }
    }

    fun addActivity(ctx: Context)
    {
        // mapper handles the serialisation of Joda date into a String.
        val mapper = jsonObjectMapper()
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDAO.save(activity)
        ctx.json(activity)
    }

    fun updateActivity(ctx: Context)
    {
        val mapper = jacksonObjectMapper()
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDAO.update(
            id = ctx.pathParam("activity-id").toInt(),
            activity = activity
        )
    }

    fun deleteActivity(ctx: Context)
    {
        activityDAO.delete(ctx.pathParam("activity-id").toInt())
    }

    fun deleteActivityByUserId(ctx: Context)
    {
        activityDAO.deleteAll(ctx.pathParam("user-id").toInt())
    }
}