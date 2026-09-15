package com.codealpha.endpointmonitor.service;

import com.codealpha.endpointmonitor.model.CheckResult;
import com.codealpha.endpointmonitor.model.MonitoredEndpoint;
import com.codealpha.endpointmonitor.repository.CheckResultRepository;
import com.codealpha.endpointmonitor.repository.MonitoredEndpointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

@Service
public class EndpointCheckerService {

    private static final Logger log = LoggerFactory.getLogger(EndpointCheckerService.class);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final CheckResultRepository checkResultRepository;
    private final MonitoredEndpointRepository endpointRepository;

    public EndpointCheckerService(CheckResultRepository checkResultRepository,
                                   MonitoredEndpointRepository endpointRepository) {
        this.checkResultRepository = checkResultRepository;
        this.endpointRepository = endpointRepository;
    }

    public void check(MonitoredEndpoint endpoint) {
        CheckResult result = new CheckResult();
        result.setEndpoint(endpoint);

        long start = System.currentTimeMillis();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint.getUrl()))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            long elapsed = System.currentTimeMillis() - start;

            result.setStatusCode(response.statusCode());
            result.setResponseTimeMs(elapsed);
            result.setUp(response.statusCode() < 400);

            log.info("Checked {} -> {} ({} ms)", endpoint.getUrl(), response.statusCode(), elapsed);
        } catch (Exception ex) {
            long elapsed = System.currentTimeMillis() - start;
            result.setStatusCode(-1);
            result.setResponseTimeMs(elapsed);
            result.setUp(false);
            result.setErrorMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());

            log.warn("Check failed for {} -> {}", endpoint.getUrl(), result.getErrorMessage());
        }

        checkResultRepository.save(result);
        endpoint.setLastCheckedAt(Instant.now());
        endpointRepository.save(endpoint);
    }
}
