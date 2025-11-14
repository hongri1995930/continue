import Vue from "vue";
import Router from "vue-router";
import AssistantDashboard from "@/views/AssistantDashboard.vue";

Vue.use(Router);

export default new Router({
  mode: "hash",
  routes: [
    {
      path: "/",
      name: "Assistants",
      component: AssistantDashboard,
    },
  ],
});
