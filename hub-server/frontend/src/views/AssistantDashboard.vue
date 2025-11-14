<template>
  <div class="dashboard">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span>助手配置列表</span>
        <div class="actions">
          <el-select v-model="selectedOrg" placeholder="选择组织" size="small" @change="fetchAssistants">
            <el-option label="个人工作区" :value="null"></el-option>
            <el-option v-for="org in organizations" :key="org.value" :label="org.label" :value="org.value" />
          </el-select>
          <el-button type="primary" icon="el-icon-refresh" size="small" @click="fetchAssistants">刷新</el-button>
        </div>
      </div>
      <el-table :data="assistants" v-loading="loading" style="width: 100%">
        <el-table-column prop="ownerSlug" label="所属" width="120" />
        <el-table-column prop="packageSlug" label="包名" width="160" />
        <el-table-column prop="configResult.config.name" label="名称" />
        <el-table-column prop="configResult.config.defaultModel" label="默认模型" width="180" />
        <el-table-column label="规则" width="220">
          <template slot-scope="scope">
            <el-tag v-for="rule in (scope.row.configResult.config.rules || [])" :key="rule.name" type="info" size="mini">
              {{ rule.name }}
            </el-tag>
            <span v-if="!scope.row.configResult.config.rules || !scope.row.configResult.config.rules.length">—</span>
          </template>
        </el-table-column>
        <el-table-column label="MCP 服务" width="220">
          <template slot-scope="scope">
            <el-tag v-for="mcp in (scope.row.configResult.config.mcpServers || [])" :key="mcp.name" type="success" size="mini">
              {{ mcp.name }}
            </el-tag>
            <span v-if="!scope.row.configResult.config.mcpServers || !scope.row.configResult.config.mcpServers.length">—</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDetails(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="detailAssistantTitle" :visible.sync="detailVisible" width="720px">
      <div v-if="detailAssistant">
        <p><strong>Raw YAML</strong></p>
        <el-input type="textarea" :value="detailAssistant.rawYaml" rows="12" readonly></el-input>
        <el-divider></el-divider>
        <p><strong>配置校验</strong></p>
        <el-alert
          v-if="hasErrors"
          title="存在配置警告"
          type="warning"
          show-icon>
          <ul>
            <li v-for="(error, index) in detailAssistant.configResult.errors" :key="index">{{ error.message }}</li>
          </ul>
        </el-alert>
        <el-alert v-else title="未发现配置错误" type="success" show-icon></el-alert>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';

export default {
  name: 'AssistantDashboard',
  data() {
    return {
      detailVisible: false,
      detailAssistant: null,
      selectedOrg: null,
      organizations: [
        { label: 'Acme 研发部', value: 'org-acme' }
      ]
    };
  },
  computed: {
    ...mapState(['assistants', 'loading']),
    detailAssistantTitle() {
      if (!this.detailAssistant) {
        return '助手详情';
      }
      return `${this.detailAssistant.configResult.config.name}（${this.detailAssistant.ownerSlug}/${this.detailAssistant.packageSlug}）`;
    },
    hasErrors() {
      return this.detailAssistant && this.detailAssistant.configResult.errors && this.detailAssistant.configResult.errors.length;
    }
  },
  created() {
    this.fetchAssistants();
  },
  methods: {
    ...mapActions(['loadAssistants']),
    async fetchAssistants() {
      await this.loadAssistants({ organizationId: this.selectedOrg });
    },
    showDetails(assistant) {
      this.detailAssistant = assistant;
      this.detailVisible = true;
    }
  }
};
</script>

<style scoped>
.dashboard {
  padding: 16px;
}

.actions {
  float: right;
  display: flex;
  gap: 12px;
  align-items: center;
}

.el-card__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-tag + .el-tag {
  margin-left: 4px;
}
</style>
