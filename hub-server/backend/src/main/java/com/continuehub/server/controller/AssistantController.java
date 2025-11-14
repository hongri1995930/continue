package com.continuehub.server.controller;

import com.continuehub.server.dto.AssistantMapper;
import com.continuehub.server.dto.AssistantResponse;
import com.continuehub.server.service.AssistantService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/ide", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @GetMapping("/list-assistants")
    public List<AssistantResponse> listAssistants(
            @RequestParam(value = "organizationId", required = false) String organizationId
    ) {
        return assistantService.listAssistants(organizationId).stream()
                .map(AssistantMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/list-assistant-full-slugs")
    public List<String> listAssistantFullSlugs(
            @RequestParam(value = "organizationId", required = false) String organizationId
    ) {
        return assistantService.listAssistantFullSlugs(organizationId);
    }
}
