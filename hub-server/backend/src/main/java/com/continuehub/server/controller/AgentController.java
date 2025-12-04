package com.continuehub.server.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/agents", produces = MediaType.APPLICATION_JSON_VALUE)
public class AgentController {

    // Stub for on-prem: no remote devboxes, return empty list to satisfy core client
    @GetMapping("/devboxes")
    public Object listDevboxes() {
        return Collections.emptyList();
    }

    // Stub tunnel endpoint; returns null url to indicate unsupported
    @PostMapping("/devboxes/{id}/tunnel")
    public ResponseEntity<?> devboxTunnel(@PathVariable("id") String id) {
        return ResponseEntity.ok(Map.of("url", ""));
    }

    /**
     * Minimal stubs to satisfy core/control-plane client expectations for background agents.
     */
    @GetMapping
    public Object listAgents(
            @RequestParam(value = "organizationId", required = false) String organizationId,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        return Map.of("agents", Collections.emptyList(), "totalCount", 0);
    }

    @PostMapping
    public Object createAgent(@RequestBody(required = false) Map<String, Object> body) {
        return Map.of("id", UUID.randomUUID().toString());
    }

    @GetMapping("/{id}")
    public Object getAgent(@PathVariable("id") String id) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("github_repo", "");

        Map<String, Object> view = new HashMap<>();
        view.put("id", id);
        view.put("devboxId", null);
        view.put("name", "Background Agent");
        view.put("icon", null);
        view.put("status", "inactive");
        view.put("agentStatus", null);
        view.put("unread", false);
        view.put("state", "");
        view.put("metadata", metadata);
        view.put("repoUrl", "");
        view.put("branch", null);
        view.put("pullRequestUrl", null);
        view.put("pullRequestStatus", null);
        view.put("tunnelUrl", null);
        String now = Instant.now().toString();
        view.put("createdAt", now);
        view.put("updatedAt", now);
        view.put("create_time_ms", String.valueOf(System.currentTimeMillis()));
        view.put("end_time_ms", String.valueOf(System.currentTimeMillis()));
        return view;
    }

    @GetMapping("/{id}/state")
    public Object getAgentState(@PathVariable("id") String id) {
        Map<String, Object> session = new HashMap<>();
        session.put("history", Collections.emptyList());
        session.put("messages", Collections.emptyList());
        session.put("title", "Background Agent");
        session.put("workspace", Collections.emptyMap());
        return Map.of(
                "session", session,
                "isProcessing", false,
                "messageQueueLength", 0,
                "pendingPermission", null
        );
    }
}
