package com.continuehub.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListPersonalAssistantsWhenOrganizationMissing() throws Exception {
        mockMvc.perform(get("/ide/list-assistants")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ownerSlug", is("personal")))
                .andExpect(jsonPath("$[0].configResult.config.defaultModel", is("openai-gpt-4o-mini")));
    }

    @Test
    void shouldListOrganizationAssistants() throws Exception {
        mockMvc.perform(get("/ide/list-assistants")
                        .queryParam("organizationId", "org-acme")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ownerSlug", is("acme")))
                .andExpect(jsonPath("$[0].configResult.config.models[0].name", is("acme-gpt")));
    }

    @Test
    void shouldListAssistantFullSlugs() throws Exception {
        mockMvc.perform(get("/ide/list-assistant-full-slugs")
                        .queryParam("organizationId", "org-acme")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is("acme/default-assistant@v1")));
    }
}
