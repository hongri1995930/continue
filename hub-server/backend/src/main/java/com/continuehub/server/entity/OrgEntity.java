package com.continuehub.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("organizations")
public class OrgEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orgSlug;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgSlug() {
        return orgSlug;
    }

    public void setOrgSlug(String orgSlug) {
        this.orgSlug = orgSlug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
