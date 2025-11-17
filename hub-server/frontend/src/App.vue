<template>
  <div class="app-shell">
    <header class="app-header">
      <div class="brand">
        <div class="brand-title">
          <span class="brand-badge">Hub</span>
          <h1>Continue Hub 配置中心</h1>
        </div>
        <p>统一管理 IDE 助手与模型策略的可视化工作台</p>
      </div>
      <div class="header-meta">
        <el-tag type="success" effect="dark" size="small">服务正常</el-tag>
        <span class="version-text">版本 v0.1.0</span>
      </div>
    </header>
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: "App",
};
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
  background: linear-gradient(180deg, #f1f5ff 0%, #f8f9fb 55%, #ffffff 100%);
}

.app-header {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 24px 16px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  color: #1f2d3d;
}

.brand-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand-badge {
  background: #1f64ff;
  color: #fff;
  font-size: 12px;
  letter-spacing: 0.05em;
  padding: 4px 10px;
  border-radius: 999px;
  text-transform: uppercase;
}

.brand h1 {
  font-size: 28px;
  margin: 0;
}

.brand p {
  margin: 6px 0 0;
  color: #5c677d;
}

.header-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 6px;
  font-size: 13px;
  color: #5c677d;
}

.app-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px 40px;
}

@media (max-width: 768px) {
  .app-header {
    flex-direction: column;
    gap: 16px;
  }

  .header-meta {
    align-items: flex-start;
  }
}
</style>
