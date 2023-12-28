<template id="activity-overview">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">All Activities</h5>
          <div class="card-body">
            <div
              class="list-group-item d-flex align-items-start"
              v-for="(activity, index) in activities"
              v-bind:key="index"
            >
              <div class="mr-auto p-2">
                <span> {{ index+1 }}. {{ activity.description }} </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("activity-overview", {
  template: "#activity-overview",
  data: () => ({
    activities: [],
  }),
  created() {
    this.fetchActivites();
  },
  methods: {
    fetchActivites: function () {
      axios
        .get("/api/activities")
        .then((res) => (this.activities = res.data))
        .catch((e) => alert("Error fetching activites!"));
    },
  },
});
</script>
