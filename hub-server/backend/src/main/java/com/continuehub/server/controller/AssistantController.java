package com.continuehub.server.controller;

import com.continuehub.server.dto.AssistantResponse;
import com.continuehub.server.dto.UpdateAssistantRequest;
import com.continuehub.server.dto.AssistantMapper;
import com.continuehub.server.security.AuthContextHolder;
import com.continuehub.server.service.AssistantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/ide", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @GetMapping("/list-assistants")
    public List<AssistantResponse> listAssistants(
            @RequestParam(value = "organizationId", required = false) String organizationId
    ) {
        System.out.println("list-assistants-------");
        String orgFromToken = Optional.ofNullable(AuthContextHolder.get()).map(ctx -> ctx.getOrgSlug()).orElse(null);
        String org = organizationId != null ? organizationId : orgFromToken;
        if (org == null || org.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "organizationId is required");
        }

        return assistantService.listAssistants(org).stream()
                .map(AssistantMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/list-assistant-full-slugs")
    public Object listAssistantFullSlugs(
            @RequestParam(value = "organizationId", required = false) String organizationId
    ) {
        System.out.println("list-assistant-full-slugs-------");
        String orgFromToken = Optional.ofNullable(AuthContextHolder.get()).map(ctx -> ctx.getOrgSlug()).orElse(null);
        String org = organizationId != null ? organizationId : orgFromToken;
        if (org == null || org.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "organizationId is required");
        }
        // core/control-plane/client.ts expects a JSON object with a "fullSlugs" field
        return java.util.Collections.singletonMap("fullSlugs", assistantService.listAssistantFullSlugs(org));
    }

    @PostMapping("/update-assistant")
    public ResponseEntity<AssistantResponse> updateAssistant(
            @Valid @RequestBody UpdateAssistantRequest request
    ) {
        String orgFromToken = Optional.ofNullable(AuthContextHolder.get()).map(ctx -> ctx.getOrgSlug()).orElse(null);
        if (orgFromToken == null || orgFromToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "organizationId is required");
        }
        return ResponseEntity.ok(
                AssistantMapper.toResponse(
                        assistantService.updateAssistant(request)
                )
        );
    }
}
