<template id="edible-overview">
    <app-layout>
        <div class="row">
            <div class="col">
                <div class="card">
                    <div>
                        <button rel="tooltip" title="Add" class="btn btn-info btn-simple btn-link"
                            style="float: right; margin-top: 1%; margin-right: 1%;" @click="addEdible()">
                            <i class="fa fa-plus" aria-hidden="true"></i>
                        </button>
                        <h5 class="card-header">All Foods</h5>
                    </div>
                    <div class="card-body">
                        <div class="list-group-item d-flex align-items-start" v-for="(edible, index) in edibles"
                            v-bind:key="edibles">
                            <div class="mr-auto p-2">
                                <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                                    style="float: right;" :key="index" @click="deleteEdible(edible.id, index)">
                                    <i class="fas fa-trash" aria-hidden="true"></i>
                                </button>
                                <span> {{ index + 1 }}. {{ edible.name }} </span><br>
                                &nbsp;&nbsp; {{ edible.calories }} Cal (per 100 grams)
                                <div class="list-group-item d-flex align-items-start">
                                    <div>
                                        <b> {{ edible.protein }}g </b>
                                        <br />
                                        <font color="orange">Protein</font>
                                    </div>
                                    &nbsp; &nbsp;
                                    <div>
                                        <b> {{ edible.fat }}g </b>
                                        <br />
                                        <font color="purple">Fat</font>
                                    </div>
                                    &nbsp; &nbsp;
                                    <div>
                                        <b> {{ edible.carb }}g </b>
                                        <br />
                                        <font color="lime">Carbs</font>
                                    </div>
                                    &nbsp; &nbsp;
                                    <div>
                                        <b> {{ edible.sugar }}g </b>
                                        <br />
                                        <font color="red">Sugar</font>
                                    </div>
                                    &nbsp; &nbsp;
                                    <div>
                                        <b> {{ edible.fiber }}g </b>
                                        <br />
                                        <font color="blue">Fiber</font>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </app-layout>
</template>
  
<script>
app.component("edible-overview", {
    template: "#edible-overview",
    data: () => ({
        edibles: [],
    }),
    created() {
        this.fetchEdibles();
    },
    methods: {
        deleteEdible: function (id, index) {
            Swal.fire({
                title: "Do you want to delete this food?",
                showCancelButton: true,
                confirmButtonText: "Delete",
            }).then((result) => {
                if (result.isConfirmed) {
                    axios
                        .delete("/api/edibles/" + id)
                        .then(res => {
                            console.log(res);
                        })
                        .catch(e => { console.log(e); })
                    this.edibles.splice(index, 1)
                }
            });
        },
        fetchEdibles: function () {
            axios
                .get("/api/edibles")
                .then((res) => (this.edibles = res.data))
                .catch((e) => alert("Error fetching edibles!"));
        },
        addEdible: function () {
            let foodName = ""
            let foodCal = 0
            let foodProtein = 0
            let foodCarb = 0
            let foodFat = 0
            let foodFiber = 0
            let foodSugar = 0

            Swal.fire({
                title: "Food's Name",
                input: "text",
                showCancelButton: true,
                inputLabel: "Please provide food's name",
                inputValidator: (value) => {
                    if (!value) {
                        return "You need to write something!";
                    }
                }
            })
                .then(res => {
                    foodName = res.value
                    if (!res.isConfirmed) {
                        return
                    }
                    Swal.fire({
                        title: "Input Calories",
                        input: "number",
                        inputLabel: "Calories of the food",
                        showCancelButton: true,
                        inputValidator: (value) => {
                            if (!value) {
                                return "Provide a meaninful value!";
                            }
                        }
                    })
                        .then(res => {
                            foodCal = res.value
                            if (!res.isConfirmed) {
                                return
                            }
                            Swal.fire({
                                title: "Input Proteins",
                                input: "number",
                                showCancelButton: true,
                                inputLabel: "Amount of food's protein",
                                inputValidator: (value) => {
                                    if (!value) {
                                        return "Provide a meaninful value!";
                                    }
                                }
                            })
                                .then(res => {
                                    foodProtein = res.value
                                    if (!res.isConfirmed) {
                                        return
                                    }

                                    Swal.fire({
                                        title: "Input Carbs",
                                        input: "number",
                                        showCancelButton: true,
                                        inputLabel: "Amount of food's carbs",
                                        inputValidator: (value) => {
                                            if (!value) {
                                                return "Provide a meaninful value!";
                                            }
                                        }
                                    })
                                        .then(res => {
                                            foodCarb = res.value
                                            if (!res.isConfirmed) {
                                                return
                                            }
                                            Swal.fire({
                                                title: "Input Fats",
                                                input: "number",
                                                showCancelButton: true,
                                                inputLabel: "Amount of food's fats",
                                                inputValidator: (value) => {
                                                    if (!value) {
                                                        return "Provide a meaninful value!";
                                                    }
                                                }
                                            })
                                                .then(res => {
                                                    foodFat = res.value
                                                    if (!res.isConfirmed) {
                                                        return
                                                    }
                                                    Swal.fire({
                                                        title: "Input Fibers",
                                                        input: "number",
                                                        showCancelButton: true,
                                                        inputLabel: "Amount of food's fiber",
                                                        inputValidator: (value) => {
                                                            if (!value) {
                                                                return "Provide a meaninful value!";
                                                            }
                                                        }
                                                    })
                                                        .then(res => {
                                                            foodFiber = res.value
                                                            if (!res.isConfirmed) {
                                                                return
                                                            }
                                                            Swal.fire({
                                                                title: "Input Sugars",
                                                                input: "number",
                                                                showCancelButton: true,
                                                                inputLabel: "Amount of food's sugar",
                                                                inputValidator: (value) => {
                                                                    if (!value) {
                                                                        return "Provide a meaninful value!";
                                                                    }
                                                                }
                                                            })
                                                                .then(res => {
                                                                    foodSugar = res.value
                                                                    if (!res.isConfirmed) {
                                                                        return
                                                                    }
                                                                    Swal.fire({
                                                                        title: "Input Unit of the Food",
                                                                        input: "number",
                                                                        showCancelButton: true,
                                                                        inputLabel: "Amount of food's unit (e.g. 100 grams)",
                                                                        inputValidator: (value) => {
                                                                            if (!value) {
                                                                                return "Provide a meaninful value!";
                                                                            }
                                                                        }
                                                                    })
                                                                        .then(res => {
                                                                            if (res.value != 100) {
                                                                                let grams = res.value
                                                                                if (!res.isConfirmed) {
                                                                                    return
                                                                                }
                                                                                foodCal = foodCal * 100 / grams
                                                                                foodProtein = foodProtein * 100 / grams
                                                                                foodCarb = foodCarb * 100 / grams
                                                                                foodFat = foodFat * 100 / grams
                                                                                foodFiber = foodFiber * 100 / grams
                                                                                foodSugar = foodSugar * 100 / grams
                                                                            }

                                                                            axios
                                                                                .post("/api/edibles", {
                                                                                    name: foodName,
                                                                                    calories: foodCal,
                                                                                    protein: foodProtein,
                                                                                    carb: foodCarb,
                                                                                    fat: foodFat,
                                                                                    fiber: foodFiber,
                                                                                    sugar: foodSugar
                                                                                })
                                                                                .then(res => {
                                                                                    console.log(res);
                                                                                })
                                                                                .catch(e => {
                                                                                    console.log(e);
                                                                                })
                                                                        })
                                                                })
                                                        })
                                                })
                                        })
                                })
                        })
                })
        }
    },
});
</script>
  