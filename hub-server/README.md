# 自建 Continue Hub 兼容服务

该目录包含一个使用 Spring Boot 2.6.15（JDK 11）实现的后端服务以及 Vue 2.5.17 + Element UI 构建的前端控制台，用于演示 Continue Hub 配置分发能力。

## 目录结构

- `backend/`：Spring Boot 应用，暴露 IDE/控制平面兼容接口，提供助手配置、登录、策略占位等。
- `frontend/`：Vue 单页应用，调用后端接口展示助手列表、模型、规则与 MCP 服务详情。

## 后端已实现的接口（按 controller）

- `AuthController`：`/auth/login`、`/auth/refresh`（简单账号+密码=123456，校验组织成员，返回 access/refresh token）。
- `AssistantController`：`GET /ide/list-assistants`、`GET /ide/list-assistant-full-slugs`（`{ fullSlugs: [...] }`）、`POST /ide/update-assistant`。
- `OrganizationController`：`GET /ide/list-organizations`，返回当前 token 所属 org。
- `AgentController`：devbox stub（`/agents/devboxes`、`/agents/devboxes/{id}/tunnel`）；背景 Agent 兼容 stub（`GET /agents`、`POST /agents`、`GET /agents/{id}`、`GET /agents/{id}/state`）。
- `ControlPlaneController`：`GET /ide/policy`、`POST /ide/sync-secrets`、`GET /ide/credits`、`GET /ide/get-models-add-on-checkout-url`，均返回占位数据以满足 IDE 调用。
- `IndexController`：`/`、`/login`、`/index*` 转发到前端 `index.html`。

## 本地运行

### 后端

```bash
cd backend
./mvnw spring-boot:run # 如仓库内无 mvnw，可使用已安装的 Maven: mvn spring-boot:run
```

服务默认监听 `8081` 端口，可通过 `src/main/resources/assistants/assistant-store.json` 修改种子数据。

### 前端

```bash
cd frontend
npm install
npm run serve
```

开发服务器默认运行在 `http://localhost:8080`，并通过 Webpack 代理将 `/ide` 请求转发至 `http://localhost:8081`。

## 配置扩展

- 在 `assistant-store.json` 中新增对象即可为不同组织或个人工作区提供新的助手。
- `organizationIds` 数组为空或缺失的助手将自动对所有个人工作区可见；若包含具体组织 ID，则仅在请求中携带相同 `organizationId` 时返回。
- `configResult` 字段遵循 Continue 插件的 `ConfigResult<AssistantUnrolled>` 结构，可逐步扩展模型、MCP、规则等配置项。
