package com.continuehub.server.model;

import java.util.Set;

public class AssistantRecord {

    private String ownerSlug;
    private String packageSlug;
    private String versionSlug;
    private String iconUrl;
    private String rawYaml;
    private ConfigResult<AssistantConfig> configResult;
    private Set<String> organizationIds;
    private boolean defaultForPersonalWorkspaces;

    public String getOwnerSlug() {
        return ownerSlug;
    }

    public void setOwnerSlug(String ownerSlug) {
        this.ownerSlug = ownerSlug;
    }

    public String getPackageSlug() {
        return packageSlug;
    }

    public void setPackageSlug(String packageSlug) {
        this.packageSlug = packageSlug;
    }

    public String getVersionSlug() {
        return versionSlug;
    }

    public void setVersionSlug(String versionSlug) {
        this.versionSlug = versionSlug;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getRawYaml() {
        return rawYaml;
    }

    public void setRawYaml(String rawYaml) {
        this.rawYaml = rawYaml;
    }

    public ConfigResult<AssistantConfig> getConfigResult() {
        return configResult;
    }

    public void setConfigResult(ConfigResult<AssistantConfig> configResult) {
        this.configResult = configResult;
    }

    public Set<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(Set<String> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public boolean isDefaultForPersonalWorkspaces() {
        return defaultForPersonalWorkspaces;
    }

    public void setDefaultForPersonalWorkspaces(boolean defaultForPersonalWorkspaces) {
        this.defaultForPersonalWorkspaces = defaultForPersonalWorkspaces;
    }

    public String getFullSlug() {
        return ownerSlug + "/" + packageSlug + "@" + versionSlug;
    }
}
