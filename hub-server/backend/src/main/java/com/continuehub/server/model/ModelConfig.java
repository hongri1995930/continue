package com.continuehub.server.model;

import java.util.List;
import java.util.Map;

public class ModelConfig {

    private String name;
    private String provider;
    private String model;
    private List<String> roles;
    private Map<String, Object> requestOptions;
    private Map<String, String> env;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Map<String, Object> getRequestOptions() {
        return requestOptions;
    }

    public void setRequestOptions(Map<String, Object> requestOptions) {
        this.requestOptions = requestOptions;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }
}
