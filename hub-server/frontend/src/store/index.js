import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    assistants: [],
    loading: false,
    saving: false,
  },
  mutations: {
    setAssistants(state, payload) {
      state.assistants = payload;
    },
    setLoading(state, flag) {
      state.loading = flag;
    },
    setSaving(state, flag) {
      state.saving = flag;
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
    async saveAssistant(
      { commit, dispatch },
      { assistant, rawYaml, organizationId },
    ) {
      commit("setSaving", true);
      try {
        await axios.post("/ide/update-assistant", {
          ownerSlug: assistant.ownerSlug,
          packageSlug: assistant.packageSlug,
          rawYaml,
        });
        await dispatch("loadAssistants", { organizationId });
      } finally {
        commit("setSaving", false);
      }
    },
  },
});
