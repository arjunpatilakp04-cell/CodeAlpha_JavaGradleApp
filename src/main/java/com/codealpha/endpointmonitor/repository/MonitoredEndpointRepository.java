package com.codealpha.endpointmonitor.repository;

import com.codealpha.endpointmonitor.model.MonitoredEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpoint, Long> {
    List<MonitoredEndpoint> findByActiveTrue();
}
