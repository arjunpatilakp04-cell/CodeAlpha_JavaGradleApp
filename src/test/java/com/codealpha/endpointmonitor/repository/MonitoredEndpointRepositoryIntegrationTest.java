package com.codealpha.endpointmonitor.repository;

import com.codealpha.endpointmonitor.AbstractIntegrationTest;
import com.codealpha.endpointmonitor.model.MonitoredEndpoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MonitoredEndpointRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MonitoredEndpointRepository repository;

    @Test
    void savesAndRetrievesEndpoint() {
        MonitoredEndpoint endpoint = new MonitoredEndpoint();
        endpoint.setName("Test Site");
        endpoint.setUrl("https://example.com");
        endpoint.setCheckIntervalSeconds(60);

        MonitoredEndpoint saved = repository.save(endpoint);

        assertThat(saved.getId()).isNotNull();

        List<MonitoredEndpoint> active = repository.findByActiveTrue();
        assertThat(active).extracting(MonitoredEndpoint::getName).contains("Test Site");
    }
}
