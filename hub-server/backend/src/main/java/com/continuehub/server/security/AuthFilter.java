package com.continuehub.server.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public AuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        // Allow unauthenticated requests to login and static assets
        if (path.startsWith("/auth/login")
                || path.startsWith("/auth/refresh")
                || path.startsWith("/static")
                || path.startsWith("/favicon")
                || path.startsWith("/index.html")
                || path.startsWith("/assets")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = authHeader.substring("Bearer ".length());
        try {
            AuthContext ctx = jwtService.parse(token);
            if (ctx.getOrgSlug() == null || ctx.getOrgSlug().isBlank()) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }
            AuthContextHolder.set(ctx);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } finally {
            AuthContextHolder.clear();
        }
    }
}
