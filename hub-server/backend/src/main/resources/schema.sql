CREATE TABLE IF NOT EXISTS assistants (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    owner_slug VARCHAR(128) NOT NULL,
    package_slug VARCHAR(128) NOT NULL,
    version_slug VARCHAR(64),
    icon_url VARCHAR(512),
    raw_yaml TEXT NOT NULL,
    organization_ids TEXT,
    default_for_personal_workspaces BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_owner_package UNIQUE (owner_slug, package_slug)
);
