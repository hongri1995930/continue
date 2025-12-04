INSERT INTO assistants (owner_slug, package_slug, version_slug, icon_url, raw_yaml, organization_ids, default_for_personal_workspaces)
VALUES
('acme', 'default-assistant', 'v1', 'https://example.com/icons/acme-default.png',
'name: "Acme Default"
version: "0.0.1"
description: "Default assistant for Acme teams"
defaultModel: "acme-gpt"
models:
  - name: "acme-gpt"
    uses: "openai/gpt-4o-mini"
    provider: "continue-proxy"
    orgScopeId: "org-acme"
    onPremProxyUrl: "http://localhost:3001"
    roles:
      - "chat"
      - "edit"
    env: {}
rules:
  - name: "security"
    rule: "所有代码修改必须包含必要的安全校验与审计日志。"
  - name: "tone"
    rule: "对用户保持专业且简洁的语气。"
', 'org-acme', false)
ON DUPLICATE KEY UPDATE
version_slug = VALUES(version_slug),
icon_url = VALUES(icon_url),
raw_yaml = VALUES(raw_yaml),
organization_ids = VALUES(organization_ids),
default_for_personal_workspaces = VALUES(default_for_personal_workspaces);

INSERT INTO assistants (owner_slug, package_slug, version_slug, icon_url, raw_yaml, organization_ids, default_for_personal_workspaces)
VALUES
('personal', 'starter', 'v3', 'https://example.com/icons/personal-starter.png',
'name: "Personal Starter"
version: "0.0.1"
description: "Personal workspace defaults"
defaultModel: "openai-gpt-4o-mini"
models:
  - name: "openai-gpt-4o-mini"
    uses: "openai-gpt-4o-mini"
    provider: "continue-proxy"
    orgScopeId: "personal"
    onPremProxyUrl: "http://localhost:3001"
    roles:
      - "chat"
      - "edit"
    env: {}
rules:
  - name: "concise"
    rule: "保持回答简洁明了，并在必要时提供代码示例。"
', '', true)
ON DUPLICATE KEY UPDATE
version_slug = VALUES(version_slug),
icon_url = VALUES(icon_url),
raw_yaml = VALUES(raw_yaml),
organization_ids = VALUES(organization_ids),
default_for_personal_workspaces = VALUES(default_for_personal_workspaces);
