package com.codealpha.endpointmonitor.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "check_results")
public class CheckResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private MonitoredEndpoint endpoint;

    private int statusCode;

    @Column(name = "response_time_ms")
    private long responseTimeMs;

    private boolean up;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt = Instant.now();

    @Column(name = "error_message")
    private String errorMessage;

    public Long getId() { return id; }
    public MonitoredEndpoint getEndpoint() { return endpoint; }
    public void setEndpoint(MonitoredEndpoint endpoint) { this.endpoint = endpoint; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    public boolean isUp() { return up; }
    public void setUp(boolean up) { this.up = up; }
    public Instant getCheckedAt() { return checkedAt; }
    public void setCheckedAt(Instant checkedAt) { this.checkedAt = checkedAt; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
