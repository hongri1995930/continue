<template>
  <div class="page">
    <div class="header">
      <div>
        <p class="eyebrow">ORGANIZATION SETTINGS</p>
        <h2>组织：{{ orgSlug }}</h2>
        <p class="subtitle">
          这里可以扩展组织级别的模型策略、MCP 服务和访问控制配置。
        </p>
      </div>
      <el-button type="text" size="small" @click="$router.push('/')">
        返回总览
      </el-button>
    </div>

    <el-card class="card">
      <el-alert
        title="占位页面"
        type="info"
        description="当前为本地 Hub 的组织设置占位页面，可根据需要补充具体表单与配置项。"
        show-icon
        :closable="false"
      />

      <el-divider />

      <el-descriptions title="组织基础信息" :column="1" border size="small">
        <el-descriptions-item label="组织标识">
          {{ orgSlug }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "OrgSettingsView",
  props: {
    orgSlug: {
      type: String,
      required: true,
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

