package ie.setu.controllers

import ie.setu.domain.Edible
import ie.setu.domain.repository.EdibleDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object EdibleController {
    private val edibleDAO = EdibleDAO()

    fun getAllEdibles(ctx: Context) {
        val edibles = edibleDAO.getAll()
        if (edibles.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(edibles)
    }

    fun getEdibleById(ctx: Context) {
        val edible = edibleDAO.findById(ctx.pathParam("edible-id").toInt())
        if (edible != null) {
            ctx.status(200)
            ctx.json(edible)
        } else {
            ctx.status(404)
        }
    }

    fun addEdible(ctx: Context) {
        val edible: Edible = jsonToObject(ctx.body())
        val edibleId = edibleDAO.save(edible)
        if (edibleId != null) {
            edible.id = edibleId
            ctx.json(edible)
            ctx.status(201)
        }
    }

    fun deleteEdible(ctx: Context) {
        if (edibleDAO.delete(ctx.pathParam("edible-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun updateEdible(ctx: Context) {
        val edible: Edible = jsonToObject(ctx.body())

        if (edibleDAO.update(ctx.pathParam("edible-id").toInt(), edible) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}