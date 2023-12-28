<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p>We're sorry, we were not able to retrieve this user.</p>
      <p>View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6">User Profile</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
          </div>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" @click="deleteUser()">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">Gender</span>
            </div>
            <input class="form-control" v-model="userTraits.gender" name="gender" readonly placeholder="Other" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">Height (CM)</span>
            </div>
            <input type="number" class="form-control" v-model="userTraits.height" name="height" readonly
              placeholder="Not Given" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">Weight (KG)</span>
            </div>
            <input type="number" class="form-control" v-model="userTraits.weight" name="weight" readonly
              placeholder="Not Given" />
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        <button rel="tooltip" title="Add" class="btn btn-info btn-simple btn-link" @click="addActivity()"
          style="float: right;">
          <i class="fa fa-plus" aria-hidden="true"></i>
        </button>
        <p v-if="activities.length === 0">No activities yet...</p>
        <p v-if="activities.length > 0">Activities</p>
        <div class="card" v-for="(activity, index) in activities" :key="renderActivities">
          <div class="card-body" style="margin-bottom: 1%;">
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" style="float: right;"
              :key="index" @click="deleteActivity(activity.id)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
            <h5 class="card-title">{{ activity.description }}</h5>
            <h6 class="card-subtitle mb-2 text-muted">
              {{ activity.duration }} minutes
            </h6>
            {{ activity.calories }} Cal burned
          </div>
        </div>
      </div>
      <div class="card-footer text-left">
        <button rel="tooltip" title="Add" class="btn btn-info btn-simple btn-link" @click="addFoodToUser()"
          style="float: right;">
          <i class="fa fa-plus" aria-hidden="true"></i>
        </button>
        <p>Foods</p>
        <p v-if="userFoods.length === 0">No Edible is logged.</p>

        <div class="card" style="margin-bottom: 2%">
          <div class="card-body">
            <h5 class="card-title">Summary</h5>
            <h6 class="card-subtitle mb-2 text-muted">
              {{ this.calorieSum }}/{{
                Math.floor(userTraits.weight * 2.20462 * 15)
              }}
              Cal
            </h6>
            <hr />
            Carbs: &nbsp;<b> {{ carbSum.toFixed(2) }} </b>/{{
              (
                (Math.floor(userTraits.weight * 2.20462 * 15) * 5) /
                10 /
                4
              ).toFixed(2)
            }}
            <div class="progress" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"
              style="margin-bottom: 1%">
              <div class="progress-bar bg-info" :style="{
                    width: (carbSum * 100 / ((Math.floor(userTraits.weight * 2.20462 * 15) * 5) / 10 / 4)) + '%'
                  }"></div>
            </div>
            Protein: &nbsp;<b> {{ proteinSum.toFixed(2) }} </b>/{{
              (userTraits.weight * 0.8).toFixed(2)
            }}
            <div class="progress" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"
              style="margin-bottom: 1%">
              <div class="progress-bar bg-info" :style="{
                    width: (proteinSum * 100 / (
                      (Math.floor(userTraits.weight * 2.20462 * 15) * 3) /
                      10 /
                      9
                    )) + '%'
                  }"></div>
            </div>
            Fat: &nbsp;<b> {{ fatSum.toFixed(2) }} </b>/{{
              (
                (Math.floor(userTraits.weight * 2.20462 * 15) * 3) /
                10 /
                9
              ).toFixed(2)
            }}
            <div class="progress" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"
              style="margin-bottom: 1%">
              <div class="progress-bar bg-info" :style="{
                    width: (fatSum * 100 / (
                      (Math.floor(userTraits.weight * 2.20462 * 15) * 1) /
                      10 /
                      4
                    )) + '%'
                  }"></div>
            </div>
            Sugar: &nbsp;<b> {{ sugarSum.toFixed(2) }} </b>/{{
              (
                (Math.floor(userTraits.weight * 2.20462 * 15) * 1) /
                10 /
                4
              ).toFixed(2)
            }}
            <div class="progress" role="progressbar" aria-valuenow=" {{ 98 }} " aria-valuemin="0" aria-valuemax="100"
              style="margin-bottom: 1%">
              <div id="progress-fiber" class="progress-bar bg-info" :style="{
                    width: (sugarSum * 100 / (
                      (Math.floor(userTraits.weight * 2.20462 * 15) * 1) /
                      10 /
                      4
                    )) + '%'
                  }"></div>
            </div>
            Fiber: &nbsp;<b>{{ fiberSum.toFixed(2) }}</b>/{{ userTraits.gender == "female" ? 25 : 38 }}
            <div class="progress" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"
              style="margin-bottom: 1%">
              <div class="progress-bar bg-info"
                :style="{ width: (fiberSum * 100 / (userTraits.gender == 'female' ? 25 : 38)) + '%' }"></div>
            </div>
          </div>
        </div>

        <div class="card" v-for="(food, index)  in  userFoods" :key="userFoods" style="margin-bottom: 1%">
          <div class="card-body">
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" style="float: right;"
              :key="index" @click="deleteUserFood(food.id, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" style="float: right;"
              @click="updateUserFood(food.id, index)">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
            <h5 class="card-title">{{ food.name }}</h5>
            <h6 class="card-subtitle mb-2 text-muted">
              {{ food.calories }} Cal
            </h6>
            <div class="list-group-item d-flex align-items-start">
              <div>
                <b> {{ food.protein }}g </b>
                <br />
                <font color="orange">Protein</font>
              </div>
              &nbsp; &nbsp;
              <div>
                <b> {{ food.fat }}g </b>
                <br />
                <font color="purple">Fat</font>
              </div>
              &nbsp; &nbsp;
              <div>
                <b> {{ food.carb }}g </b>
                <br />
                <font color="lime">Carbs</font>
              </div>
              &nbsp; &nbsp;
              <div>
                <b> {{ food.sugar }}g </b>
                <br />
                <font color="red">Sugar</font>
              </div>
              &nbsp; &nbsp;
              <div>
                <b> {{ food.fiber }}g </b>
                <br />
                <font color="blue">Fiber</font>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    userTraits: [],
    userFoods: [],
    userFoodIds: [],
    calorieSum: 0,
    carbSum: 0,
    fatSum: 0,
    proteinSum: 0,
    fiberSum: 0,
    sugarSum: 0,
    renderActivities: 0
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`;
    axios
      .get(url)
      .then((res) => (this.user = res.data))
      .catch((error) => {
        console.log(
          "No user found for id passed in the path parameter: " + error
        );
        this.noUserFound = true;
      });
    axios
      .get(url + `/activities`)
      .then((res) => (this.activities = res.data))
      .catch((error) => {
        console.log("No activities added yet (this is ok): " + error);
      });

    this.fetchUserTrait();

    this.fetchUserFoodsIds();
  },
  methods: {
    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`;
      axios
        .patch(url, {
          name: this.user.name,
          email: this.user.email,
        })
        .then((response) => this.user.push(response.data))
        .catch((error) => {
          console.log(error);
        });
      alert("User updated!");
    },
    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`;
        axios
          .delete(url)
          .then((response) => {
            alert("User deleted");
            //display the /users endpoint
            window.location.href = "/users";
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    },
    fetchUserTrait: function () {
      const userId = this.$javalin.pathParams["user-id"];
      axios
        .get("/api/userTrait/" + userId)
        .then((res) => (this.userTraits = res.data))
        .catch((e) => {
          console.log(e);
        });
    },
    fetchUserFoodsIds: async function () {
      const userId = this.$javalin.pathParams["user-id"];
      await axios
        .get("/api/userEdible/user/" + userId)
        .then((res) => {
          let _data = res.data;

          _data.forEach((element) => {
            this.userFoodIds.push({ id: element["edibleId"], grams: element["grams"] });
          });
        })
        .catch((e) => console.log(e));

      this.fetchUserFoods();
    },
    fetchUserFoods: async function () {
      await this.userFoodIds.forEach((e) => {
        axios
          .get("/api/edibles/" + e["id"])
          .then((res) => {
            let foodGram = e['grams'];
            let foodDetails = res.data;

            res.data.calories = foodGram / 100 * foodDetails.calories
            res.data.protein = foodGram / 100 * foodDetails.protein
            res.data.fat = foodGram / 100 * foodDetails.fat
            res.data.carb = foodGram / 100 * foodDetails.carb
            res.data.sugar = foodGram / 100 * foodDetails.sugar
            res.data.fiber = foodGram / 100 * foodDetails.fiber

            this.userFoods.push(res.data);
            this.calorieSum += res.data.calories;
            this.carbSum += res.data.carb;
            this.fatSum += res.data.fat;
            this.proteinSum += res.data.protein;
            this.fiberSum += res.data.fiber;
            this.sugarSum += res.data.sugar;
          })
          .catch((e) => console.log(e));
      });
    },
    deleteUserFood: function (id, index) {
      Swal.fire({
        title: "Do you want to delete this food?",
        showCancelButton: true,
        confirmButtonText: "Delete",
      }).then((result) => {
        if (result.isConfirmed) {
          let food = this.userFoods[index]

          this.calorieSum -= food.calories;
          this.carbSum -= food.carb;
          this.fatSum -= food.fat;
          this.proteinSum -= food.protein;
          this.fiberSum -= food.fiber;
          this.sugarSum -= food.sugar;

          axios
            .delete("/api/userEdible/edible/" + id)
            .then(res => {
              console.log(res);
            })
            .catch(e => { console.log(e); })

          this.userFoods.splice(index, 1)
        }
      });
    },
    updateUserFood: function (id, index) {
      Swal.fire({
        title: 'Login Form',
        html: `<input type="number" id="grams" class="swal2-input" placeholder="grams">`,
        confirmButtonText: 'Update',
        focusConfirm: false,
        preConfirm: () => {
          const gramsinput = grams.value
          if (!gramsinput) {
            Swal.showValidationMessage(`Please enter a value!`)
          }
          return gramsinput
        },
      })
        .then(result => {
          if (result.isConfirmed) {
            axios
              .get('/api/userEdible/edible/' + id)
              .then((response) => {
                console.log(response.data);
                let food = this.userFoods[index]
                let currentGram = response.data[0].grams
                let foodGram = result.value

                axios
                  .patch("/api/userEdible/edible/" + id, {
                    userId: this.user.id,
                    edibleId: id,
                    grams: result.value
                  })
                  .then(res => {
                    food.calories = foodGram / currentGram * food.calories
                    food.protein = foodGram / currentGram * food.protein
                    food.fat = foodGram / currentGram * food.fat
                    food.carb = foodGram / currentGram * food.carb
                    food.sugar = foodGram / currentGram * food.sugar
                    food.fiber = foodGram / currentGram * food.fiber

                    if (currentGram < foodGram) {
                      this.calorieSum += food.calories;
                      this.carbSum += food.carb;
                      this.fatSum += food.fat;
                      this.proteinSum += food.protein;
                      this.fiberSum += food.fiber;
                      this.sugarSum += food.sugar;
                    }
                    else {
                      this.calorieSum -= food.calories;
                      this.carbSum -= food.carb;
                      this.fatSum -= food.fat;
                      this.proteinSum -= food.protein;
                      this.fiberSum -= food.fiber;
                      this.sugarSum -= food.sugar;
                    }
                  })
                  .catch(e => console.log(e))
              })
              .catch((error) => {
                console.error(error);
              });
          }
        })
    },
    addFoodToUser: async function () {
      let allFoods = []
      let foods = []

      await axios.get("/api/edibles").then(res => {
        res.data.forEach(e => {
          allFoods.push(e["name"])
          foods.push(e)
        })
      }).catch(e => console.log(e))

      Swal.fire({
        title: "Select Food",
        input: "select",
        inputOptions: allFoods,
        inputPlaceholder: "Please select",
        showCancelButton: true,
        inputValidator: (value) => {
          return new Promise((resolve) => {
            if (value == "") {
              resolve("Please select a food!");
            }
            else {
              resolve();
            }
          });
        }
      })
        .then(res => {
          let foodId = foods[res.value].id

          Swal.fire({
            title: "Input Grams",
            input: "number",
            inputLabel: "Amount of the food",
            inputValidator: (value) => {
              if (!value || value <= 0) {
                return "Provide a meaninful value!";
              }
            }
          })
            .then(res => {
              axios
                .post("/api/userEdible", {
                  userId: this.$javalin.pathParams["user-id"],
                  edibleId: foodId,
                  grams: res.value
                })
                .then(res => {
                  console.log(res);
                })
                .catch(e => console.log(e))
            })
        })
    },
    addActivity: function () {
      let actDesc = ""
      let actDuration = 0
      let actCals = 0

      Swal.fire({
        title: "Activity Description",
        input: "text",
        inputLabel: "Describe the Activity",
        inputValidator: (value) => {
          if (!value) {
            return "You need to write something!";
          }
        }
      })
        .then(res => {
          console.log(res);
          actDesc = res.value

          Swal.fire({
            title: "Input Duration",
            input: "number",
            inputLabel: "Duration of the Activity",
            inputValidator: (value) => {
              if (!value || value <= 0) {
                return "Provide a meaninful value!";
              }
            }
          })
            .then(res => {
              actDuration = res.value

              Swal.fire({
                title: "Input Calories Burned",
                input: "number",
                inputValidator: (value) => {
                  if (!value || value <= 0) {
                    return "Provide a meaninful value!";
                  }
                }
              })
                .then(res => {
                  actCals = res.value

                  axios
                    .post("/api/activities", {
                      description: actDesc,
                      duration: actDuration,
                      calories: actCals,
                      started: Date.now(),
                      userId: this.$javalin.pathParams["user-id"]
                    })
                    .then(res => {
                      console.log(res)
                      this.renderActivities += 1
                    })
                    .catch(e => console.log(e))
                })
            })
        })
    },
    deleteActivity: function (id) {
      Swal.fire({
        title: "Do you want to delete this activity?",
        showCancelButton: true,
        confirmButtonText: "Delete",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .delete("/api/activities/" + id)
            .then(e => {
              console.log(e);
              renderActivities += 1
            })
            .catch(e => console.log(e))
        }
      })
    }
  },
});
</script>
