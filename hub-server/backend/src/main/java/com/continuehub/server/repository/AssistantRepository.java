package com.continuehub.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.continuehub.server.entity.AssistantEntity;
import com.continuehub.server.model.AssistantRecord;
import com.continuehub.server.model.ConfigError;
import com.continuehub.server.model.ConfigResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class AssistantRepository {

    private final AssistantEntityMapper assistantMapper;
    private final ObjectMapper objectMapper;
    private final Yaml yamlParser = new Yaml();

    public AssistantRepository(AssistantEntityMapper assistantMapper, ObjectMapper objectMapper) {
        this.assistantMapper = assistantMapper;
        this.objectMapper = objectMapper;
    }

    public List<AssistantRecord> findByOrganizationId(String organizationId) {
        List<AssistantEntity> entities = assistantMapper.selectList(new LambdaQueryWrapper<>());
        return entities.stream()
                .map(this::toRecord)
                .filter(record -> matchesOrganization(record, organizationId))
                .collect(Collectors.toList());
    }

    public List<String> findFullSlugsByOrganizationId(String organizationId) {
        return findByOrganizationId(organizationId).stream()
                .map(AssistantRecord::getFullSlug)
                .collect(Collectors.toList());
    }

    public List<AssistantRecord> findAll() {
        return Collections.unmodifiableList(
                assistantMapper.selectList(new LambdaQueryWrapper<>()).stream()
                        .map(this::toRecord)
                        .collect(Collectors.toList())
        );
    }

    public AssistantRecord updateAssistant(String ownerSlug, String packageSlug, String rawYaml) {
        AssistantEntity target = assistantMapper.selectOne(
                new LambdaQueryWrapper<AssistantEntity>()
                        .eq(AssistantEntity::getOwnerSlug, ownerSlug)
                        .eq(AssistantEntity::getPackageSlug, packageSlug)
        );
        if (target == null) {
            throw new IllegalArgumentException("Assistant not found");
        }

        target.setRawYaml(rawYaml);
        assistantMapper.updateById(target);
        return toRecord(target);
    }

    private AssistantRecord toRecord(AssistantEntity entity) {
        AssistantRecord record = new AssistantRecord();
        record.setOwnerSlug(entity.getOwnerSlug());
        record.setPackageSlug(entity.getPackageSlug());
        record.setVersionSlug(entity.getVersionSlug());
        record.setIconUrl(entity.getIconUrl());
        record.setRawYaml(entity.getRawYaml());
        record.setOrganizationIds(parseOrganizationIds(entity.getOrganizationIds()));
        record.setDefaultForPersonalWorkspaces(entity.isDefaultForPersonalWorkspaces());
        record.setConfigResult(parseYaml(entity.getRawYaml(), entity.getOwnerSlug() + "/" + entity.getPackageSlug()));
        return record;
    }

    private boolean matchesOrganization(AssistantRecord record, String organizationId) {
        Set<String> orgIds = Optional.ofNullable(record.getOrganizationIds()).orElse(Collections.emptySet());
        if (organizationId == null || organizationId.isBlank()) {
            return record.isDefaultForPersonalWorkspaces() || orgIds.isEmpty();
        }
        return orgIds.contains(organizationId);
    }

    private Set<String> parseOrganizationIds(String csv) {
        if (csv == null || csv.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(HashSet::new));
    }

    private ConfigResult<Object> parseYaml(String rawYaml, String uri) {
        ConfigResult<Object> result = new ConfigResult<>();
        try {
            Object loaded = yamlParser.load(rawYaml);
            // Pass through the parsed YAML structure directly so it stays
            // compatible with the AssistantUnrolled schema used by the IDE.
            result.setConfig(loaded);
            result.setErrors(Collections.emptyList());
            result.setConfigLoadInterrupted(false);
        } catch (Exception e) {
            ConfigError error = new ConfigError();
            error.setFatal(true);
            error.setMessage("Failed to parse YAML: " + e.getMessage());
            error.setUri(uri);
            result.setErrors(Collections.singletonList(error));
            result.setConfigLoadInterrupted(true);
            result.setConfig(null);
        }
        return result;
    }
}
