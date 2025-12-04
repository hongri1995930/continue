package com.continuehub.server.security;

public class AuthContext {
    private final String username;
    private final String orgSlug;

    public AuthContext(String username, String orgSlug) {
        this.username = username;
        this.orgSlug = orgSlug;
    }

    public String getUsername() {
        return username;
    }

    public String getOrgSlug() {
        return orgSlug;
    }
}
