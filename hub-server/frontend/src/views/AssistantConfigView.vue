<template>
  <div class="page">
    <div class="header">
      <div>
        <p class="eyebrow">ASSISTANT CONFIG</p>
        <h2>{{ title }}</h2>
        <p class="subtitle">
          {{ subtitle }}
        </p>
      </div>
      <el-button type="text" size="small" @click="$router.push('/')">
        返回总览
      </el-button>
    </div>

    <el-card v-if="assistant" class="card">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="名称">
          {{ assistant.configResult.config.name || "未命名" }}
        </el-descriptions-item>
        <el-descriptions-item label="包标识">
          {{ assistant.ownerSlug }}/{{ assistant.packageSlug }}
        </el-descriptions-item>
        <el-descriptions-item label="版本">
          {{ assistant.configResult.config.version || "未设置" }}
        </el-descriptions-item>
        <el-descriptions-item label="默认模型">
          {{ assistant.configResult.config.defaultModel || "未配置" }}
        </el-descriptions-item>
      </el-descriptions>

      <el-divider></el-divider>

      <h4>Raw YAML</h4>
      <el-input
        type="textarea"
        :rows="18"
        :value="assistant.rawYaml || '暂无原始 YAML'"
        readonly
        spellcheck="false"
      />
    </el-card>

    <el-empty
      v-else
      description="未找到对应助手，请检查 ownerSlug / packageSlug 是否正确。"
    />
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";

export default {
  name: "AssistantConfigView",
  props: {
    ownerSlug: {
      type: String,
      required: true,
    },
    packageSlug: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      assistant: null,
      lastLoadedOrg: null,
    };
  },
  computed: {
    ...mapState(["assistants", "loading"]),
    title() {
      return this.assistant?.configResult?.config?.name || "助手配置详情";
    },
    subtitle() {
      return `${this.ownerSlug}/${this.packageSlug}`;
    },
  },
  async created() {
    await this.ensureDataLoaded();
    this.pickAssistant();
  },
  methods: {
    ...mapActions(["loadAssistants"]),
    async ensureDataLoaded() {
      // 从 localStorage 读取 orgSlug，确保与 IDE 一致
      const orgSlug = localStorage.getItem("orgSlug") || null;
      if (this.assistants.length === 0 || this.lastLoadedOrg !== orgSlug) {
        await this.loadAssistants({ organizationId: orgSlug });
        this.lastLoadedOrg = orgSlug;
      }
    },
    pickAssistant() {
      this.assistant =
        this.assistants.find(
          (a) =>
            a.ownerSlug === this.ownerSlug && a.packageSlug === this.packageSlug,
        ) || null;
    },
  },
  watch: {
    assistants() {
      this.pickAssistant();
    },
  },
};
</script>

<style scoped>
.page {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.eyebrow {
  font-size: 12px;
  letter-spacing: 0.18em;
  color: #5c6ac4;
  margin: 0 0 4px;
}

.subtitle {
  color: #5c677d;
  margin-top: 4px;
}

.card {
  max-width: 960px;
}
</style>

