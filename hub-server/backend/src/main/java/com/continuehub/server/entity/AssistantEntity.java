package com.continuehub.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("assistants")
public class AssistantEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String ownerSlug;
    private String packageSlug;
    private String versionSlug;
    private String iconUrl;
    private String rawYaml;
    private String organizationIds;
    private boolean defaultForPersonalWorkspaces;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(String organizationIds) {
        this.organizationIds = organizationIds;
    }

    public boolean isDefaultForPersonalWorkspaces() {
        return defaultForPersonalWorkspaces;
    }

    public void setDefaultForPersonalWorkspaces(boolean defaultForPersonalWorkspaces) {
        this.defaultForPersonalWorkspaces = defaultForPersonalWorkspaces;
    }
}
