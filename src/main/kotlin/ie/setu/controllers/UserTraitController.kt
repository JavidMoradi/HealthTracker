package ie.setu.controllers

import ie.setu.domain.UserTrait
import ie.setu.domain.repository.UserTraitDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object UserTraitController {
    private val userTraitDAO = UserTraitDAO()

    fun getAll(ctx: Context) {
        val userTraits = userTraitDAO.getAll()
        if (userTraits.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(userTraits)
    }

    fun findByUserId(ctx: Context) {
        val userTrait = userTraitDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (userTrait != null) {
            ctx.json(userTrait)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addUserTrait(ctx: Context) {
        val userTrait = jsonToObject<UserTrait>(ctx.body())
        val userTraitId = userTraitDAO.save(userTrait)
        if (userTraitId != null) {
            userTrait.userId = userTraitId
            ctx.json(userTrait)
            ctx.status(201)
        }
    }

    fun deleteUserTrait(ctx: Context) {
        if (userTraitDAO.delete(ctx.pathParam("user-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun updateUserTrait(ctx: Context) {
        val userTrait: UserTrait = jsonToObject(ctx.body())

        if (userTraitDAO.update(ctx.pathParam("user-id").toInt(), userTrait) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}