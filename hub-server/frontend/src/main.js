import Vue from "vue";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from "axios";

Vue.config.productionTip = false;
Vue.use(ElementUI);

// Base URL for API (configured via .env.*)
axios.defaults.baseURL = process.env.VUE_APP_API_BASE || "";

// Restore token from localStorage for axios
const savedToken = localStorage.getItem("authToken");
if (savedToken) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${savedToken}`;
  // 同步默认 org 参数，便于统一传递
  const org = localStorage.getItem("orgSlug");
  if (org) {
    axios.defaults.params = axios.defaults.params || {};
    axios.defaults.params.organizationId = org;
  }
}

// Simple navigation guard: require login except /login
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("authToken");
  if (to.path !== "/login" && !token) {
    next({ path: "/login" });
    return;
  }
  if (token) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    const org = localStorage.getItem("orgSlug");
    axios.defaults.params = axios.defaults.params || {};
    if (org) {
      axios.defaults.params.organizationId = org;
    } else {
      delete axios.defaults.params.organizationId;
    }
  } else {
    delete axios.defaults.headers.common["Authorization"];
    if (axios.defaults.params) {
      delete axios.defaults.params.organizationId;
    }
  }
  next();
});

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
