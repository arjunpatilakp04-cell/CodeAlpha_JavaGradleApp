package com.codealpha.endpointmonitor.service;

import com.codealpha.endpointmonitor.dto.CreateEndpointRequest;
import com.codealpha.endpointmonitor.dto.EndpointResponse;
import com.codealpha.endpointmonitor.dto.UpdateEndpointRequest;
import com.codealpha.endpointmonitor.exception.ResourceNotFoundException;
import com.codealpha.endpointmonitor.model.MonitoredEndpoint;
import com.codealpha.endpointmonitor.repository.CheckResultRepository;
import com.codealpha.endpointmonitor.repository.MonitoredEndpointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitoredEndpointServiceTest {

    @Mock
    private MonitoredEndpointRepository endpointRepository;

    @Mock
    private CheckResultRepository checkResultRepository;

    @InjectMocks
    private MonitoredEndpointService service;

    private MonitoredEndpoint sample;

    @BeforeEach
    void setUp() {
        sample = new MonitoredEndpoint();
        sample.setName("Example");
        sample.setUrl("https://example.com");
        sample.setCheckIntervalSeconds(60);
    }

    @Test
    void create_savesAndReturnsEndpoint() {
        when(endpointRepository.save(any(MonitoredEndpoint.class))).thenReturn(sample);

        CreateEndpointRequest request = new CreateEndpointRequest();
        request.setName("Example");
        request.setUrl("https://example.com");
        request.setCheckIntervalSeconds(60);

        EndpointResponse response = service.create(request);

        assertThat(response.getName()).isEqualTo("Example");
        verify(endpointRepository).save(any(MonitoredEndpoint.class));
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(endpointRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_updatesFieldsAndSaves() {
        when(endpointRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(endpointRepository.save(any(MonitoredEndpoint.class))).thenReturn(sample);

        UpdateEndpointRequest request = new UpdateEndpointRequest();
        request.setName("Renamed");
        request.setUrl("https://renamed.com");
        request.setCheckIntervalSeconds(120);
        request.setActive(false);

        service.update(1L, request);

        assertThat(sample.getName()).isEqualTo("Renamed");
        assertThat(sample.isActive()).isFalse();
        verify(endpointRepository).save(sample);
    }

    @Test
    void delete_removesEndpoint() {
        when(endpointRepository.findById(1L)).thenReturn(Optional.of(sample));

        service.delete(1L);

        verify(endpointRepository).delete(sample);
    }

    @Test
    void getUptimePercentage_returnsZeroWhenNoChecksRecorded() {
        when(endpointRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(checkResultRepository.countByEndpointId(1L)).thenReturn(0L);

        double uptime = service.getUptimePercentage(1L);

        assertThat(uptime).isEqualTo(0.0);
    }

    @Test
    void getUptimePercentage_calculatesCorrectPercentage() {
        when(endpointRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(checkResultRepository.countByEndpointId(1L)).thenReturn(4L);
        when(checkResultRepository.countByEndpointIdAndUpTrue(1L)).thenReturn(3L);

        double uptime = service.getUptimePercentage(1L);

        assertThat(uptime).isEqualTo(75.0);
    }
}
