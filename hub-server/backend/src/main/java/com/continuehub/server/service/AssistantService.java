package com.continuehub.server.service;

import com.continuehub.server.model.AssistantRecord;
import com.continuehub.server.repository.AssistantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssistantService {

    private final AssistantRepository assistantRepository;

    public AssistantService(AssistantRepository assistantRepository) {
        this.assistantRepository = assistantRepository;
    }

    public List<AssistantRecord> listAssistants(String organizationId) {
        return assistantRepository.findByOrganizationId(organizationId);
    }

    public List<String> listAssistantFullSlugs(String organizationId) {
        return assistantRepository.findFullSlugsByOrganizationId(organizationId);
    }
}
