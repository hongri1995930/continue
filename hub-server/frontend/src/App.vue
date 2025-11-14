<template>
  <div id="layout">
    <el-container style="height: 100vh;">
      <el-header height="56px">
        <div class="header">
          <h1>Continue Hub 配置中心</h1>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
};
</script>

<style scoped>
#layout {
  background: #f5f7fa;
}

.header {
  display: flex;
  align-items: center;
  height: 100%;
}

.header h1 {
  font-size: 20px;
  margin: 0;
  color: #303133;
}
</style>
