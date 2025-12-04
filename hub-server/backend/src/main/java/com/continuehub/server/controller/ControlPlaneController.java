package com.continuehub.server.controller;

import com.continuehub.server.security.AuthContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * Minimal control-plane compatible endpoints so IDE plugins can pull org-wide settings from a self-hosted hub.
 */
@RestController
@RequestMapping(path = "/ide", produces = MediaType.APPLICATION_JSON_VALUE)
public class ControlPlaneController {

    @GetMapping("/policy")
    public Object getPolicy() {
        var ctx = AuthContextHolder.get();
        return Map.of(
                "orgSlug", ctx != null ? ctx.getOrgSlug() : null,
                "policy", Collections.emptyMap()
        );
    }

    @PostMapping("/sync-secrets")
    public Object syncSecrets(@RequestBody(required = false) Map<String, Object> body) {
        // Return empty/not-found secrets; clients treat this gracefully.
        return Collections.emptyList();
    }

    @GetMapping("/credits")
    public Object getCredits() {
        return Map.of(
                "optedInToFreeTrial", false,
                "hasCredits", false,
                "creditBalance", 0,
                "hasPurchasedCredits", false
        );
    }

    @GetMapping("/get-models-add-on-checkout-url")
    public Object getModelsCheckoutUrl() {
        return Map.of("url", "");
    }
}
