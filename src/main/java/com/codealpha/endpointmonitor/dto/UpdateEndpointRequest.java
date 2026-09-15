package com.codealpha.endpointmonitor.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class UpdateEndpointRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "url is required")
    @URL(message = "url must be a valid URL")
    private String url;

    @Min(value = 10, message = "checkIntervalSeconds must be at least 10")
    private int checkIntervalSeconds;

    private boolean active;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public int getCheckIntervalSeconds() { return checkIntervalSeconds; }
    public void setCheckIntervalSeconds(int checkIntervalSeconds) { this.checkIntervalSeconds = checkIntervalSeconds; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
