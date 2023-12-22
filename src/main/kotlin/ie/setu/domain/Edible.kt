package ie.setu.domain

data class Edible(
        var id: Int,
        var calories: Int,
        var protein: Double,
        var carb: Double,
        var fat: Double,
        var fiber: Double,
        var sugar: Double
)