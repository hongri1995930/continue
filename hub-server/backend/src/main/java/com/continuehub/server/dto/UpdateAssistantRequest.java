package com.continuehub.server.dto;

import javax.validation.constraints.NotBlank;

public class UpdateAssistantRequest {

    @NotBlank
    private String ownerSlug;

    @NotBlank
    private String packageSlug;

    @NotBlank
    private String rawYaml;

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

    public String getRawYaml() {
        return rawYaml;
    }

    public void setRawYaml(String rawYaml) {
        this.rawYaml = rawYaml;
    }
}
