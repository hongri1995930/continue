<template>
  <div class="login-page">
    <el-card class="login-card">
      <h3 class="title">Continue Hub 登录</h3>
      <el-form :model="form" label-position="top" @submit.native.prevent="onSubmit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" autocomplete="current-password" />
        </el-form-item>
        <el-form-item label="组织">
          <el-input v-model="form.orgSlug" placeholder="例如：org-acme" />
        </el-form-item>
        <el-button type="primary" :loading="loading" @click="onSubmit" style="width: 100%">
          登录
        </el-button>
      </el-form>
    </el-card>

    <el-dialog
      title="复制 Token 到 IDE"
      :visible.sync="tokenDialogVisible"
      width="520px"
      append-to-body
      @close="onTokenDialogClosed"
    >
      <p>请将下方 Token 粘贴到 IDEA/VSC 登录弹窗中：</p>
      <el-input type="textarea" :rows="3" v-model="issuedToken" readonly></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="tokenDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Login",
  data() {
    return {
      loading: false,
      form: {
        username: "admin",
        password: "123456",
        orgSlug: "org-acme",
      },
      tokenDialogVisible: false,
      issuedToken: "",
      redirectAfterToken: false,
    };
  },
  methods: {
    async onSubmit() {
      if (!this.form.username || !this.form.password || !this.form.orgSlug) {
        this.$message.error("请填写完整信息");
        return;
      }
      this.loading = true;
      try {
        const resp = await axios.post("/auth/login", {
          username: this.form.username,
          password: this.form.password,
          orgSlug: this.form.orgSlug,
        });
        const { token, orgSlug } = resp.data || {};
        if (!token) {
          throw new Error("登录失败：未返回 token");
        }
        localStorage.setItem("authToken", token);
        localStorage.setItem("orgSlug", orgSlug || "");
        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        if (orgSlug) {
          axios.defaults.params = axios.defaults.params || {};
          axios.defaults.params.organizationId = orgSlug;
        }
        // 展示 token 供 IDE 插件手动粘贴
        this.issuedToken = token;
        this.tokenDialogVisible = true;
        this.redirectAfterToken = true;
        try {
          await navigator.clipboard.writeText(token);
          this.$message.success("登录成功，Token 已复制到剪贴板，请回 IDE 粘贴。");
        } catch (e) {
          this.$message.success("登录成功，请复制 Token 粘贴到 IDE。");
        }
      } catch (err) {
        this.$message.error(err?.message || "登录失败");
      } finally {
        this.loading = false;
      }
    },
    onTokenDialogClosed() {
      this.tokenDialogVisible = false;
      if (this.redirectAfterToken) {
        this.redirectAfterToken = false;
        this.$router.replace({ path: "/" });
      }
    },
  },
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #eef2ff 0%, #f8fafc 100%);
}

.login-card {
  width: 360px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
}

.title {
  margin: 0 0 16px;
  text-align: center;
  color: #1f2d3d;
}
</style>
