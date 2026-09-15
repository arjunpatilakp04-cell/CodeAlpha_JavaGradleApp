package com.codealpha.endpointmonitor.dto;

import java.time.Instant;

public class CheckResultResponse {
    private Long id;
    private int statusCode;
    private long responseTimeMs;
    private boolean up;
    private Instant checkedAt;
    private String errorMessage;

    public CheckResultResponse(Long id, int statusCode, long responseTimeMs, boolean up,
                                Instant checkedAt, String errorMessage) {
        this.id = id;
        this.statusCode = statusCode;
        this.responseTimeMs = responseTimeMs;
        this.up = up;
        this.checkedAt = checkedAt;
        this.errorMessage = errorMessage;
    }

    public Long getId() { return id; }
    public int getStatusCode() { return statusCode; }
    public long getResponseTimeMs() { return responseTimeMs; }
    public boolean isUp() { return up; }
    public Instant getCheckedAt() { return checkedAt; }
    public String getErrorMessage() { return errorMessage; }
}
