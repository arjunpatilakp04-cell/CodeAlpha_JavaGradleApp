package com.codealpha.endpointmonitor.repository;

import com.codealpha.endpointmonitor.model.CheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {
    List<CheckResult> findByEndpointIdOrderByCheckedAtDesc(Long endpointId, Pageable pageable);
    long countByEndpointId(Long endpointId);
    long countByEndpointIdAndUpTrue(Long endpointId);
}
