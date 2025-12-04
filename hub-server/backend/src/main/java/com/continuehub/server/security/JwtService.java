package com.continuehub.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    @Value("${security.jwt.secret:change-me}")
    private String secret;

    @Value("${security.jwt.ttlSeconds:86400}")
    private long ttlSeconds;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = toSecureKey(secret);
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String username, String orgSlug) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttlSeconds * 1000);
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("org", orgSlug))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public AuthContext parse(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        String orgSlug = claims.get("org", String.class);
        return new AuthContext(username, orgSlug);
    }

    private byte[] toSecureKey(String raw) {
        try {
            // Derive a 256-bit key using SHA-256 to satisfy HS256 key length requirements
            byte[] derived = MessageDigest.getInstance("SHA-256")
                    .digest(raw.getBytes(StandardCharsets.UTF_8));
            return derived;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }
}
