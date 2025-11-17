<template>
  <div class="dashboard">
    <section class="hero-card">
      <div>
        <p class="eyebrow">ASSISTANT OVERVIEW</p>
        <h2>助手配置一目了然</h2>
        <p class="subtitle">覆盖组织、模型策略、MCP 服务等关键信息，支持快速筛选并查看详情。</p>
        <div class="meta-line">
          <span>最新同步：{{ lastUpdatedText }}</span>
          <span class="dot" />
          <span>共 {{ assistants.length }} 个助手</span>
        </div>
      </div>
      <div class="hero-actions">
        <el-button plain size="small" icon="el-icon-refresh" @click="fetchAssistants">刷新</el-button>
        <el-button type="primary" size="small" icon="el-icon-view" @click="toggleDetailPreview" :disabled="!assistants.length">
          快速预览
        </el-button>
      </div>
    </section>

    <section class="summary-grid">
      <div
        v-for="card in summaryCards"
        :key="card.title"
        class="summary-card"
      >
        <div class="summary-icon" :style="{ background: card.bg }">
          <i :class="card.icon"></i>
        </div>
        <div>
          <p class="summary-label">{{ card.title }}</p>
          <p class="summary-value">{{ card.value }}</p>
          <p class="summary-desc">{{ card.desc }}</p>
        </div>
      </div>
    </section>

    <section class="panel">
      <div class="panel-toolbar">
        <div class="filters">
          <el-select v-model="selectedOrg" placeholder="选择组织" size="small" @change="fetchAssistants" clearable>
            <el-option label="个人工作区" :value="null" />
            <el-option
              v-for="org in organizations"
              :key="org.value"
              :label="org.label"
              :value="org.value"
            />
          </el-select>
          <el-input
            v-model="keyword"
            placeholder="搜索名称 / 包名 / 所属"
            size="small"
            clearable
            prefix-icon="el-icon-search"
          />
          <el-select v-model="modelFilter" placeholder="模型类型" size="small" clearable>
            <el-option
              v-for="model in modelOptions"
              :key="model"
              :label="model"
              :value="model"
            />
          </el-select>
        </div>
        <el-button type="text" size="small" @click="resetFilters">重置筛选</el-button>
      </div>

      <el-table
        :data="filteredAssistants"
        v-loading="loading"
        style="width: 100%"
        header-cell-class-name="table-header"
        :row-class-name="rowClassName"
        size="small"
        stripe
      >
        <template slot="empty">
          <el-empty description="未找到满足条件的助手">
            <el-button type="primary" size="mini" @click="resetFilters">清空筛选</el-button>
          </el-empty>
        </template>
        <el-table-column label="助手" min-width="200">
          <template slot-scope="scope">
            <div class="assistant-cell">
              <p class="assistant-name">{{ scope.row.configResult.config.name || '未命名助手' }}</p>
              <p class="assistant-meta">{{ scope.row.ownerSlug }} / {{ scope.row.packageSlug }}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="默认模型" width="160">
          <template slot-scope="scope">
            <el-tag effect="plain" size="mini">{{ scope.row.configResult.config.defaultModel || '未配置' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="规则" min-width="200">
          <template slot-scope="scope">
            <template v-if="scope.row.configResult.config.rules && scope.row.configResult.config.rules.length">
              <el-tag
                v-for="rule in scope.row.configResult.config.rules"
                :key="rule.name"
                type="info"
                size="mini"
                class="tag-chip"
              >
                {{ rule.name }}
              </el-tag>
            </template>
            <span v-else class="placeholder">—</span>
          </template>
        </el-table-column>
        <el-table-column label="MCP 服务" min-width="200">
          <template slot-scope="scope">
            <template v-if="scope.row.configResult.config.mcpServers && scope.row.configResult.config.mcpServers.length">
              <el-tag
                v-for="mcp in scope.row.configResult.config.mcpServers"
                :key="mcp.name"
                type="success"
                size="mini"
                class="tag-chip"
              >
                {{ mcp.name }}
              </el-tag>
            </template>
            <span v-else class="placeholder">—</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row)" effect="dark" size="mini">
              {{ statusText(scope.row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="showDetails(scope.row)">查看详情</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button type="text" size="mini" @click="openEditor(scope.row)">编辑配置</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog :title="detailAssistantTitle" :visible.sync="detailVisible" width="760px" :close-on-click-modal="false">
      <div v-if="detailAssistant">
        <el-tabs v-model="detailTab">
          <el-tab-pane label="结构化信息" name="meta">
            <el-descriptions :column="2" border size="small">
              <el-descriptions-item label="名称">{{ detailAssistant.configResult.config.name || '未命名' }}</el-descriptions-item>
              <el-descriptions-item label="包标识">{{ detailAssistant.ownerSlug }}/{{ detailAssistant.packageSlug }}</el-descriptions-item>
              <el-descriptions-item label="默认模型">{{ detailAssistant.configResult.config.defaultModel || '未配置' }}</el-descriptions-item>
              <el-descriptions-item label="规则数量">{{ (detailAssistant.configResult.config.rules || []).length }}</el-descriptions-item>
              <el-descriptions-item label="MCP 服务">{{ (detailAssistant.configResult.config.mcpServers || []).length }}</el-descriptions-item>
              <el-descriptions-item label="组织">{{ selectedOrgLabel }}</el-descriptions-item>
            </el-descriptions>
            <el-divider></el-divider>
            <div class="detail-section">
              <h4>规则列表</h4>
              <p v-if="!(detailAssistant.configResult.config.rules || []).length" class="placeholder">暂无规则</p>
              <el-timeline v-else>
                <el-timeline-item
                  v-for="rule in detailAssistant.configResult.config.rules"
                  :key="rule.name"
                  type="info"
                  :timestamp="rule.trigger || '策略'"
                >
                  <strong>{{ rule.name }}</strong>
                  <p class="timeline-desc">{{ rule.description || '无描述' }}</p>
                </el-timeline-item>
              </el-timeline>
            </div>
          </el-tab-pane>
          <el-tab-pane label="Raw YAML" name="yaml">
            <el-input type="textarea" :value="detailAssistant.rawYaml || '暂无原始 YAML'" rows="14" readonly></el-input>
          </el-tab-pane>
        </el-tabs>

        <el-divider></el-divider>
        <p><strong>配置校验</strong></p>
        <el-alert
          v-if="hasErrors"
          title="存在配置警告"
          type="warning"
          show-icon
        >
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

    <el-dialog :title="editDialogTitle" :visible.sync="editVisible" width="880px" :close-on-click-modal="false">
      <div v-if="editingAssistant">
        <el-alert type="info" show-icon title="根据 Continue 配置参考进行在线编辑" description="保存前会自动校验 YAML 格式，保存成功后会刷新列表。更多字段可参考官方文档 https://docs.continue.dev/reference。" />
        <el-form label-position="top" style="margin-top: 16px;">
          <el-form-item label="配置 YAML">
            <el-input
              type="textarea"
              :rows="18"
              v-model="editContent"
              spellcheck="false"
              placeholder="粘贴或编辑 Continue 配置"
            />
          </el-form-item>
        </el-form>
        <el-alert v-if="editError" type="error" show-icon :title="editError" style="margin-top: 8px;" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetEdit">重置为示例</el-button>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveEdit">保存并刷新</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';
import yaml from 'js-yaml';

export default {
  name: 'AssistantDashboard',
  data() {
    return {
      detailVisible: false,
      detailAssistant: null,
      selectedOrg: null,
      organizations: [
        { label: 'Acme 研发部', value: 'org-acme' }
      ],
      keyword: '',
      modelFilter: null,
      detailTab: 'meta',
      lastRefreshed: null,
      editVisible: false,
      editingAssistant: null,
      editContent: '',
      editError: ''
    };
  },
  computed: {
    ...mapState(['assistants', 'loading', 'saving']),
    filteredAssistants() {
      return this.assistants.filter(assistant => {
        const text = `${assistant.configResult.config.name || ''} ${assistant.packageSlug} ${assistant.ownerSlug}`.toLowerCase();
        const hitKeyword = !this.keyword || text.includes(this.keyword.trim().toLowerCase());
        const model = assistant.configResult.config.defaultModel;
        const hitModel = !this.modelFilter || model === this.modelFilter;
        return hitKeyword && hitModel;
      });
    },
    detailAssistantTitle() {
      if (!this.detailAssistant) {
        return '助手详情';
      }
      return `${this.detailAssistant.configResult.config.name}（${this.detailAssistant.ownerSlug}/${this.detailAssistant.packageSlug}）`;
    },
    editDialogTitle() {
      if (!this.editingAssistant) return '编辑配置';
      const name = this.editingAssistant.configResult.config.name || '未命名助手';
      return `编辑：${name}`;
    },
    hasErrors() {
      return this.detailAssistant && this.detailAssistant.configResult.errors && this.detailAssistant.configResult.errors.length;
    },
    summaryCards() {
      const total = this.assistants.length;
      const withRules = this.assistants.filter(item => (item.configResult.config.rules || []).length).length;
      const withMCP = this.assistants.filter(item => (item.configResult.config.mcpServers || []).length).length;
      const risky = this.assistants.filter(item => item.configResult.errors && item.configResult.errors.length).length;
      return [
        { title: '助手总数', value: total, desc: '当前已登记的助手数量', icon: 'el-icon-collection', bg: '#eef2ff' },
        { title: '已配置规则', value: withRules, desc: '包含自定义策略的助手', icon: 'el-icon-s-operation', bg: '#e0f2fe' },
        { title: '连接 MCP', value: withMCP, desc: '已挂载外部能力的助手', icon: 'el-icon-link', bg: '#ecfdf3' },
        { title: '告警条目', value: risky, desc: '存在校验提示的助手', icon: 'el-icon-warning-outline', bg: '#fef3c7' }
      ];
    },
    modelOptions() {
      const models = new Set(this.assistants.map(item => item.configResult.config.defaultModel).filter(Boolean));
      return Array.from(models);
    },
    lastUpdatedText() {
      if (!this.lastRefreshed) {
        return '尚未刷新';
      }
      return new Date(this.lastRefreshed).toLocaleString();
    },
    selectedOrgLabel() {
      if (!this.selectedOrg) {
        return '个人工作区';
      }
      const target = this.organizations.find(org => org.value === this.selectedOrg);
      return target ? target.label : this.selectedOrg;
    },
    defaultTemplate() {
      return [
        'name: MyProject',
        'version: 0.0.1',
        'schema: v1',
        'models:',
        '  - uses: anthropic/claude-3.5-sonnet',
        '    with:',
        '      ANTHROPIC_API_KEY: ${{ secrets.ANTHROPIC_API_KEY }}',
        '    override:',
        '      defaultCompletionOptions:',
        '        temperature: 0.8',
        '  - name: GPT-4',
        '    provider: openai',
        '    model: gpt-4',
        '    roles:',
        '      - chat',
        '      - edit',
        '    defaultCompletionOptions:',
        '      temperature: 0.5',
        '      maxTokens: 2000',
        '    requestOptions:',
        '      headers:',
        '        Authorization: Bearer YOUR_OPENAI_API_KEY',
        '  - name: Ollama Starcoder',
        '    provider: ollama',
        '    model: starcoder',
        '    roles:',
        '      - autocomplete',
        '    autocompleteOptions:',
        '      debounceDelay: 350',
        '      maxPromptTokens: 1024',
        '      onlyMyCode: true',
        '    defaultCompletionOptions:',
        '      temperature: 0.3',
        '      stop:',
        '        - "\\n"',
        'rules:',
        '  - Give concise responses',
        '  - Always assume TypeScript rather than JavaScript',
        'prompts:',
        '  - name: test',
        '    description: Unit test a function',
        '    prompt: |',
        '      Please write a complete suite of unit tests for this function. You should use the Jest testing framework.',
        '      The tests should cover all possible edge cases and should be as thorough as possible.',
        '      You should also include a description of each test case.',
        '  - uses: myprofile/my-favorite-prompt',
        'context:',
        '  - provider: diff',
        '  - provider: file',
        '  - provider: code',
        'mcpServers:',
        '  - name: DevServer',
        '    command: npm',
        '    args:',
        '      - run',
        '      - dev',
        '    env:',
        '      PORT: "3000"',
        'data:',
        '  - name: My Private Company',
        '    destination: https://mycompany.com/ingest',
        '    schema: 0.2.0',
        '    level: noCode',
        '    events:',
        '      - autocomplete',
        '      - chatInteraction'
      ].join('\\n');
    }
  },
  created() {
    this.fetchAssistants();
  },
  methods: {
    ...mapActions(['loadAssistants', 'saveAssistant']),
    async fetchAssistants() {
      await this.loadAssistants({ organizationId: this.selectedOrg });
      this.lastRefreshed = Date.now();
    },
    showDetails(assistant) {
      this.detailAssistant = assistant;
      this.detailTab = 'meta';
      this.detailVisible = true;
    },
    openEditor(assistant) {
      this.editingAssistant = assistant;
      this.editContent = assistant.rawYaml || this.defaultTemplate;
      this.editError = '';
      this.editVisible = true;
    },
    resetFilters() {
      this.keyword = '';
      this.modelFilter = null;
      this.selectedOrg = null;
      this.fetchAssistants();
    },
    rowClassName({ rowIndex }) {
      return rowIndex % 2 === 0 ? 'row--even' : '';
    },
    statusTagType(assistant) {
      if (assistant.configResult.errors && assistant.configResult.errors.length) {
        return 'warning';
      }
      if ((assistant.configResult.config.rules || []).length) {
        return 'success';
      }
      return 'info';
    },
    statusText(assistant) {
      if (assistant.configResult.errors && assistant.configResult.errors.length) {
        return '需关注';
      }
      if ((assistant.configResult.config.rules || []).length) {
        return '已优化';
      }
      return '基础配置';
    },
    toggleDetailPreview() {
      if (!this.assistants.length) {
        return;
      }
      this.showDetails(this.assistants[0]);
    },
    async saveEdit() {
      this.editError = '';
      if (!this.editContent.trim()) {
        this.editError = '配置内容不能为空';
        return;
      }
      try {
        yaml.load(this.editContent);
      } catch (err) {
        this.editError = `YAML 解析失败：${err.message}`;
        return;
      }
      try {
        await this.saveAssistant({
          assistant: this.editingAssistant,
          rawYaml: this.editContent,
          organizationId: this.selectedOrg,
        });
        this.$message.success('配置已保存并刷新');
        this.editVisible = false;
      } catch (err) {
        this.editError = err.message || '保存失败，请稍后重试';
      }
    },
    resetEdit() {
      this.editContent = this.defaultTemplate;
      this.editError = '';
    }
  }
};
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.hero-card {
  background: radial-gradient(circle at top left, #eef2ff, #fff);
  border: 1px solid #edf1ff;
  border-radius: 16px;
  padding: 28px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 10px 50px rgba(31, 100, 255, 0.08);
}

.eyebrow {
  font-size: 12px;
  letter-spacing: 0.18em;
  color: #5c6ac4;
  margin: 0 0 6px;
}

.hero-card h2 {
  margin: 0;
  font-size: 26px;
  color: #1f2d3d;
}

.subtitle {
  color: #5c677d;
  margin: 6px 0 12px;
}

.meta-line {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #8492a6;
  font-size: 13px;
}

.meta-line .dot {
  width: 4px;
  height: 4px;
  background: #c0ccda;
  border-radius: 50%;
}

.hero-actions {
  display: flex;
  gap: 12px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.summary-card {
  display: flex;
  gap: 14px;
  padding: 18px;
  border-radius: 12px;
  background: #fff;
  border: 1px solid #eef1f5;
}

.summary-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #1f2d3d;
  font-size: 20px;
}

.summary-label {
  margin: 0;
  color: #7c8aab;
  font-size: 12px;
  letter-spacing: 0.08em;
}

.summary-value {
  margin: 4px 0 2px;
  font-size: 24px;
  font-weight: 600;
}

.summary-desc {
  margin: 0;
  color: #97a3c0;
  font-size: 12px;
}

.panel {
  background: #fff;
  border-radius: 16px;
  padding: 0 0 16px;
  border: 1px solid #eef1f5;
  box-shadow: 0 8px 30px rgba(15, 23, 42, 0.05);
}

.panel-toolbar {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f2f7;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.filters {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.assistant-cell {
  display: flex;
  flex-direction: column;
}

.assistant-name {
  margin: 0;
  font-weight: 600;
  color: #1f2d3d;
}

.assistant-meta {
  margin: 2px 0 0;
  font-size: 12px;
  color: #8492a6;
}

.tag-chip {
  margin-right: 6px;
  margin-bottom: 4px;
}

.placeholder {
  color: #c0c4cc;
}

.detail-section h4 {
  margin: 0 0 8px;
}

.timeline-desc {
  margin: 4px 0 0;
  color: #76839b;
}

/* Element UI 的行/头节点由子组件生成，需要使用深度选择符才能生效 */
::v-deep(.table-header) {
  background: #fbfcff;
  color: #6b7280;
  font-weight: 600;
}

::v-deep(.row--even) {
  background: #fafbff;
}

@media (max-width: 768px) {
  .hero-card {
    flex-direction: column;
    align-items: flex-start;
  }

  .hero-actions {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>
