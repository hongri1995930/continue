package com.continuehub.server.model;

import java.util.List;

public class ConfigResult<T> {

    private T config;
    private List<ConfigError> errors;
    private boolean configLoadInterrupted;

    public T getConfig() {
        return config;
    }

    public void setConfig(T config) {
        this.config = config;
    }

    public List<ConfigError> getErrors() {
        return errors;
    }

    public void setErrors(List<ConfigError> errors) {
        this.errors = errors;
    }

    public boolean isConfigLoadInterrupted() {
        return configLoadInterrupted;
    }

    public void setConfigLoadInterrupted(boolean configLoadInterrupted) {
        this.configLoadInterrupted = configLoadInterrupted;
    }
}
