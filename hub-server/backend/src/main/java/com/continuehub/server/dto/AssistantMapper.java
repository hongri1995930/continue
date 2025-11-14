package com.continuehub.server.dto;

import com.continuehub.server.model.AssistantRecord;

public final class AssistantMapper {

    private AssistantMapper() {
    }

    public static AssistantResponse toResponse(AssistantRecord record) {
        AssistantResponse response = new AssistantResponse();
        response.setConfigResult(record.getConfigResult());
        response.setOwnerSlug(record.getOwnerSlug());
        response.setPackageSlug(record.getPackageSlug());
        response.setIconUrl(record.getIconUrl());
        response.setRawYaml(record.getRawYaml());
        return response;
    }
}
