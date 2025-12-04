package com.continuehub.server.controller;

import com.continuehub.server.security.AuthContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/ide", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {

    @GetMapping("/list-organizations")
    public Map<String, Object> listOrganizations() {
        var ctx = AuthContextHolder.get();
        if (ctx == null || ctx.getOrgSlug() == null || ctx.getOrgSlug().isBlank()) {
            return Map.of("organizations", Collections.emptyList());
        }

        List<Map<String, Object>> organizations = Collections.singletonList(
                Map.of(
                        "id", ctx.getOrgSlug(),
                        "name", ctx.getOrgSlug(),
                        "slug", ctx.getOrgSlug()
                )
        );
        System.out.println("list organazation"+organizations.toString());
        return Map.of("organizations", organizations);
    }
}
