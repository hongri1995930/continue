/*
 * 简易本地 Mock API（无第三方依赖），端口 8081，与前端 devServer 代理保持一致。
 * 支持：
 *  - GET  /ide/list-assistants
 *  - POST /ide/update-assistant { ownerSlug, packageSlug, rawYaml }
 * 数据持久化在 mock-data/assistants.json（相对项目根目录）。
 */

const http = require("http");
const fs = require("fs");
const path = require("path");

const PORT = process.env.MOCK_PORT || 8081;
const DATA_FILE = path.resolve(__dirname, "../mock-data/assistants.json");

function readData() {
  try {
    const raw = fs.readFileSync(DATA_FILE, "utf-8");
    return JSON.parse(raw);
  } catch (err) {
    return [];
  }
}

function writeData(data) {
  fs.mkdirSync(path.dirname(DATA_FILE), { recursive: true });
  fs.writeFileSync(DATA_FILE, JSON.stringify(data, null, 2));
}

function send(res, status, payload) {
  res.writeHead(status, {
    "Content-Type": "application/json; charset=utf-8",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET,POST,OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type",
  });
  res.end(JSON.stringify(payload));
}

function parseBody(req) {
  return new Promise((resolve) => {
    let body = "";
    req.on("data", (chunk) => (body += chunk));
    req.on("end", () => {
      try {
        resolve(JSON.parse(body || "{}"));
      } catch (err) {
        resolve({});
      }
    });
  });
}

const server = http.createServer(async (req, res) => {
  if (req.method === "OPTIONS") {
    send(res, 200, { ok: true });
    return;
  }

  if (req.url.startsWith("/ide/list-assistants")) {
    const data = readData();
    send(res, 200, data);
    return;
  }

  if (req.url.startsWith("/ide/update-assistant") && req.method === "POST") {
    const body = await parseBody(req);
    const { ownerSlug, packageSlug, rawYaml } = body;
    if (!ownerSlug || !packageSlug || !rawYaml) {
      send(res, 400, { message: "ownerSlug / packageSlug / rawYaml 不能为空" });
      return;
    }
    const data = readData();
    const existingIndex = data.findIndex(
      (item) =>
        item.ownerSlug === ownerSlug && item.packageSlug === packageSlug,
    );
    const now = new Date().toISOString();
    const base = body.configResult || { config: {}, errors: [] };
    const record = {
      ownerSlug,
      packageSlug,
      rawYaml,
      updatedAt: now,
      configResult: base,
    };
    if (existingIndex >= 0) {
      data[existingIndex] = { ...data[existingIndex], ...record };
    } else {
      data.push(record);
    }
    writeData(data);
    send(res, 200, { success: true, updatedAt: now });
    return;
  }

  send(res, 404, { message: "Not Found" });
});

server.listen(PORT, () => {
  console.log(`Mock API running at http://localhost:${PORT}`);
});
