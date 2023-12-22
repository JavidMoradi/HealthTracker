package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
        id = it[Users.id],
        name = it[Users.name],
        email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
        id = it[Activities.id],
        description = it[Activities.description],
        duration = it[Activities.duration],
        started = it[Activities.started],
        calories = it[Activities.calories],
        userId = it[Activities.userId]
)

fun mapToEdible(it: ResultRow) = Edible(
        id = it[Edibles.id],
        calories = it[Edibles.calories],
        protein = it[Edibles.protein].toDouble(),
        carb = it[Edibles.carb].toDouble(),
        fat = it[Edibles.fat].toDouble(),
        fiber = it[Edibles.fiber].toDouble(),
        sugar = it[Edibles.sugar].toDouble()
)

fun mapToUserTrait(it: ResultRow) = UserTrait(
        userId = it[UserTraits.userId],
        height = it[UserTraits.height].toDouble(),
        weight = it[UserTraits.weight].toDouble(),
        gender = it[UserTraits.gender]
)

fun mapToUserEdible(it: ResultRow) = UserEdible(
        userId = it[UserEdibles.userId],
        edibleId = it[UserEdibles.edibleId]
)