package com.codealpha.endpointmonitor.service;

import com.codealpha.endpointmonitor.dto.*;
import com.codealpha.endpointmonitor.exception.ResourceNotFoundException;
import com.codealpha.endpointmonitor.model.MonitoredEndpoint;
import com.codealpha.endpointmonitor.repository.CheckResultRepository;
import com.codealpha.endpointmonitor.repository.MonitoredEndpointRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoredEndpointService {

    private final MonitoredEndpointRepository endpointRepository;
    private final CheckResultRepository checkResultRepository;

    public MonitoredEndpointService(MonitoredEndpointRepository endpointRepository,
                                     CheckResultRepository checkResultRepository) {
        this.endpointRepository = endpointRepository;
        this.checkResultRepository = checkResultRepository;
    }

    public EndpointResponse create(CreateEndpointRequest request) {
        MonitoredEndpoint endpoint = new MonitoredEndpoint();
        endpoint.setName(request.getName());
        endpoint.setUrl(request.getUrl());
        endpoint.setCheckIntervalSeconds(request.getCheckIntervalSeconds());
        return toResponse(endpointRepository.save(endpoint));
    }

    public List<EndpointResponse> findAll() {
        return endpointRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public EndpointResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public EndpointResponse update(Long id, UpdateEndpointRequest request) {
        MonitoredEndpoint endpoint = getOrThrow(id);
        endpoint.setName(request.getName());
        endpoint.setUrl(request.getUrl());
        endpoint.setCheckIntervalSeconds(request.getCheckIntervalSeconds());
        endpoint.setActive(request.isActive());
        return toResponse(endpointRepository.save(endpoint));
    }

    public void delete(Long id) {
        endpointRepository.delete(getOrThrow(id));
    }

    public List<CheckResultResponse> getHistory(Long id, int limit) {
        getOrThrow(id);
        return checkResultRepository.findByEndpointIdOrderByCheckedAtDesc(id, PageRequest.of(0, limit))
                .stream()
                .map(r -> new CheckResultResponse(r.getId(), r.getStatusCode(), r.getResponseTimeMs(),
                        r.isUp(), r.getCheckedAt(), r.getErrorMessage()))
                .collect(Collectors.toList());
    }

    public double getUptimePercentage(Long id) {
        getOrThrow(id);
        long total = checkResultRepository.countByEndpointId(id);
        if (total == 0) return 0.0;
        long up = checkResultRepository.countByEndpointIdAndUpTrue(id);
        return Math.round((up * 10000.0 / total)) / 100.0;
    }

    private MonitoredEndpoint getOrThrow(Long id) {
        return endpointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endpoint not found: " + id));
    }

    private EndpointResponse toResponse(MonitoredEndpoint e) {
        return new EndpointResponse(e.getId(), e.getName(), e.getUrl(), e.getCheckIntervalSeconds(),
                e.isActive(), e.getCreatedAt(), e.getLastCheckedAt());
    }
}
