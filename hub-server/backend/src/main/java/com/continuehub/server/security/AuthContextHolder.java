package com.continuehub.server.security;

public class AuthContextHolder {
    private static final ThreadLocal<AuthContext> CTX = new ThreadLocal<>();

    public static void set(AuthContext authContext) {
        CTX.set(authContext);
    }

    public static AuthContext get() {
        return CTX.get();
    }

    public static void clear() {
        CTX.remove();
    }
}
