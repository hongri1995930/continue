import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    assistants: [],
    loading: false,
  },
  mutations: {
    setAssistants(state, payload) {
      state.assistants = payload;
    },
    setLoading(state, flag) {
      state.loading = flag;
    },
  },
  actions: {
    async loadAssistants({ commit }, { organizationId } = {}) {
      commit("setLoading", true);
      try {
        const params = {};
        if (organizationId) {
          params.organizationId = organizationId;
        }
        const response = await axios.get("/ide/list-assistants", { params });
        commit("setAssistants", response.data);
      } finally {
        commit("setLoading", false);
      }
    },
  },
});
