package ie.setu.helpers

import ie.setu.domain.*
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val validEmail2 = "testuser2@test.com"

val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val updatedDescription = "Updated description"

val validDesc = "description"
val validDuration = 1.0
val validCalories = 100
val validStartDate = DateTime.now()
val validUserId = 1

val validCals = 100
val validProtein = 10.0
val validCarbs = 10.0
val validFat = 10.0
val validFiber = 10.0
val validSugar = 10.0

val validEdibleId = 1
val validGrams = 100.0

val updateUserId = 2
val updatedEdibleId = 2

val validGender = "female"
val validHeight = 180.0
val validWeight = 100.0

val updatedGender = "male"

val users = arrayListOf<User>(
        User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
        User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
        User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
        User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
        Activity(id = 1, description = "Description 1", duration = 1.0, calories = 100, started = DateTime.now(), userId = 1),
        Activity(id = 2, description = "Description 2", duration = 2.0, calories = 200, started = DateTime.now(), userId = 2),
        Activity(id = 3, description = "Description 3", duration = 3.0, calories = 300, started = DateTime.now(), userId = 3),
        Activity(id = 4, description = "Description 4", duration = 4.0, calories = 400, started = DateTime.now(), userId = 4)
)

val edibles = arrayListOf<Edible>(
        Edible(id = 1, name = "edible1", calories = 100, protein = 1.0, carb = 1.0, fat = 1.0, fiber = 1.0, sugar = 1.0),
        Edible(id = 2, name = "edible2", calories = 200, protein = 2.0, carb = 2.0, fat = 2.0, fiber = 2.0, sugar = 2.0),
        Edible(id = 3, name = "edible3", calories = 300, protein = 3.0, carb = 3.0, fat = 3.0, fiber = 3.0, sugar = 3.0),
)

val userTraits = arrayListOf<UserTrait>(
        UserTrait(userId = 1, gender = "male", height = 100.0, weight = 100.0),
        UserTrait(userId = 2, gender = "female", height = 200.0, weight = 200.0),
        UserTrait(userId = 3, gender = "female", height = 300.0, weight = 300.0),
)

val userEdibles = arrayListOf<UserEdible>(
        UserEdible(userId = 1, edibleId = 1, grams = 100.0),
        UserEdible(userId = 2, edibleId = 2, grams = 200.0),
        UserEdible(userId = 3, edibleId = 3, grams = 300.0),
)