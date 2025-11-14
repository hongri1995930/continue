package com.continuehub.server.repository;

import com.continuehub.server.model.AssistantRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AssistantRepository {

    private final ObjectMapper objectMapper;
    private List<AssistantRecord> cachedAssistants = new ArrayList<>();

    public AssistantRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadAssistants() throws IOException {
        ClassPathResource resource = new ClassPathResource("assistants/assistant-store.json");
        if (!resource.exists()) {
            this.cachedAssistants = Collections.emptyList();
            return;
        }

        try (InputStream inputStream = resource.getInputStream()) {
            this.cachedAssistants = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<AssistantRecord>>() {}
            );
        }
    }

    public List<AssistantRecord> findByOrganizationId(String organizationId) {
        if (organizationId == null || organizationId.isBlank()) {
            return cachedAssistants.stream()
                    .filter(record -> record.isDefaultForPersonalWorkspaces() || record.getOrganizationIds() == null || record.getOrganizationIds().isEmpty())
                    .collect(Collectors.toList());
        }

        return cachedAssistants.stream()
                .filter(record -> record.getOrganizationIds() != null && record.getOrganizationIds().contains(organizationId))
                .collect(Collectors.toList());
    }

    public List<String> findFullSlugsByOrganizationId(String organizationId) {
        return findByOrganizationId(organizationId).stream()
                .map(AssistantRecord::getFullSlug)
                .collect(Collectors.toList());
    }

    public List<AssistantRecord> findAll() {
        return Collections.unmodifiableList(cachedAssistants);
    }
}
