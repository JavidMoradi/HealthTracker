package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.User
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val validEmail2 = "testuser2@test.com"

val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val updatedDescription = "Updated description"

val validDesc = "valid description"
val validDuration = 1.0
val validCalories = 100
val validStartDate = DateTime.now()
val validUserId = 1

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
