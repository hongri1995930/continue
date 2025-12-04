import Vue from "vue";
import Router from "vue-router";
import AssistantDashboard from "@/views/AssistantDashboard.vue";
import AssistantConfigView from "@/views/AssistantConfigView.vue";
import OrgSettingsView from "@/views/OrgSettingsView.vue";
import Login from "@/views/Login.vue";

Vue.use(Router);

const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/login",
      name: "Login",
      component: Login,
    },
    {
      path: "/",
      name: "Assistants",
      component: AssistantDashboard,
    },
    {
      path: "/organizations/:orgSlug/settings",
      name: "OrgSettings",
      component: OrgSettingsView,
      props: true,
    },
    {
      path: "/:ownerSlug/:packageSlug",
      name: "AssistantConfig",
      component: AssistantConfigView,
      props: true,
    },
    {
      path: "*",
      redirect: "/",
    },
  ],
});

export default router;
