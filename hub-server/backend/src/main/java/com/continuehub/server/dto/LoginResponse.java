package com.continuehub.server.dto;

public class LoginResponse {
    private String token;
    private String orgSlug;

    public LoginResponse(String token, String orgSlug) {
        this.token = token;
        this.orgSlug = orgSlug;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrgSlug() {
        return orgSlug;
    }

    public void setOrgSlug(String orgSlug) {
        this.orgSlug = orgSlug;
    }
}
