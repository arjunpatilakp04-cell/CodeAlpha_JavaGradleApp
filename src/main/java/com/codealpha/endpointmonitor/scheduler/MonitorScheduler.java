package com.codealpha.endpointmonitor.scheduler;

import com.codealpha.endpointmonitor.model.MonitoredEndpoint;
import com.codealpha.endpointmonitor.repository.MonitoredEndpointRepository;
import com.codealpha.endpointmonitor.service.EndpointCheckerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class MonitorScheduler {

    private final MonitoredEndpointRepository endpointRepository;
    private final EndpointCheckerService checkerService;

    public MonitorScheduler(MonitoredEndpointRepository endpointRepository,
                             EndpointCheckerService checkerService) {
        this.endpointRepository = endpointRepository;
        this.checkerService = checkerService;
    }

    @Scheduled(fixedRate = 15000)
    public void runDueChecks() {
        List<MonitoredEndpoint> activeEndpoints = endpointRepository.findByActiveTrue();
        Instant now = Instant.now();

        for (MonitoredEndpoint endpoint : activeEndpoints) {
            boolean due = endpoint.getLastCheckedAt() == null ||
                    endpoint.getLastCheckedAt().plusSeconds(endpoint.getCheckIntervalSeconds()).isBefore(now);
            if (due) {
                checkerService.check(endpoint);
            }
        }
    }
}
