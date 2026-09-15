package com.codealpha.endpointmonitor.dto;

import java.time.Instant;

public class EndpointResponse {
    private Long id;
    private String name;
    private String url;
    private int checkIntervalSeconds;
    private boolean active;
    private Instant createdAt;
    private Instant lastCheckedAt;

    public EndpointResponse(Long id, String name, String url, int checkIntervalSeconds,
                             boolean active, Instant createdAt, Instant lastCheckedAt) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.checkIntervalSeconds = checkIntervalSeconds;
        this.active = active;
        this.createdAt = createdAt;
        this.lastCheckedAt = lastCheckedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUrl() { return url; }
    public int getCheckIntervalSeconds() { return checkIntervalSeconds; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastCheckedAt() { return lastCheckedAt; }
}
