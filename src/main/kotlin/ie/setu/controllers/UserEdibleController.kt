package ie.setu.controllers

import ie.setu.domain.UserEdible
import ie.setu.domain.repository.UserEdibleDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object UserEdibleController {
    private val userEdibleDAO = UserEdibleDAO()

    fun getAll(ctx: Context) {
        val userEdibles = userEdibleDAO.getAll()
        if (userEdibles.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(userEdibles)
    }

    fun getUserEdibleByUserId(ctx: Context) {
        val userEdible = userEdibleDAO.findByUserId(ctx.pathParam("userEdible-userid").toInt())
        if (userEdible.size != 0) {
            ctx.json(userEdible)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getUserEdibleByEdibleId(ctx: Context) {
        val userEdible = userEdibleDAO.findByEdibleId(ctx.pathParam("userEdible-edibleid").toInt())
        if (userEdible.size != 0) {
            ctx.json(userEdible)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addUserEdible(ctx: Context) {
        val userEdible: UserEdible = jsonToObject(ctx.body())
        val userEdibleId = userEdibleDAO.save(userEdible)
        if (userEdibleId != null) {
            userEdible.userId = userEdibleId
            ctx.json(userEdible)
            ctx.status(201)
        }
    }

    fun updateUserEdibleByUserId(ctx: Context) {
        val userEdible: UserEdible = jsonToObject(ctx.body())

        if (userEdibleDAO.updateByUserId(ctx.pathParam("userEdible-userid").toInt(), userEdible) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun updateUserEdibleByEdibleId(ctx: Context) {
        val userEdible: UserEdible = jsonToObject(ctx.body())

        if (userEdibleDAO.updateByEdibleId(ctx.pathParam("userEdible-edibleid").toInt(), userEdible) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun deleteUserEdibleByUserId(ctx: Context) {
        if (userEdibleDAO.deleteByUserId(ctx.pathParam("userEdible-userid").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun deleteUserEdibleByEdibleId(ctx: Context) {
        if (userEdibleDAO.deleteByEdibleId(ctx.pathParam("userEdible-edibleid").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}