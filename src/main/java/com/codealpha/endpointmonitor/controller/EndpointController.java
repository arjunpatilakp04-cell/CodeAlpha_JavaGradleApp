package com.codealpha.endpointmonitor.controller;

import com.codealpha.endpointmonitor.dto.*;
import com.codealpha.endpointmonitor.service.MonitoredEndpointService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/endpoints")
public class EndpointController {

    private final MonitoredEndpointService service;

    public EndpointController(MonitoredEndpointService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EndpointResponse> create(@Valid @RequestBody CreateEndpointRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<EndpointResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EndpointResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public EndpointResponse update(@PathVariable Long id, @Valid @RequestBody UpdateEndpointRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    public List<CheckResultResponse> history(@PathVariable Long id,
                                              @RequestParam(defaultValue = "20") int limit) {
        return service.getHistory(id, limit);
    }

    @GetMapping("/{id}/uptime")
    public Map<String, Object> uptime(@PathVariable Long id) {
        return Map.of("endpointId", id, "uptimePercentage", service.getUptimePercentage(id));
    }
}
