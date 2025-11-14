package com.continuehub.server.dto;

import com.continuehub.server.model.AssistantConfig;
import com.continuehub.server.model.ConfigResult;

public class AssistantResponse {

    private ConfigResult<AssistantConfig> configResult;
    private String ownerSlug;
    private String packageSlug;
    private String iconUrl;
    private String rawYaml;

    public ConfigResult<AssistantConfig> getConfigResult() {
        return configResult;
    }

    public void setConfigResult(ConfigResult<AssistantConfig> configResult) {
        this.configResult = configResult;
    }

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
}
