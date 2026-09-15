package com.codealpha.endpointmonitor.service;

import com.codealpha.endpointmonitor.model.CheckResult;
import com.codealpha.endpointmonitor.model.MonitoredEndpoint;
import com.codealpha.endpointmonitor.repository.CheckResultRepository;
import com.codealpha.endpointmonitor.repository.MonitoredEndpointRepository;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetSocketAddress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EndpointCheckerServiceTest {

    @Mock
    private CheckResultRepository checkResultRepository;

    @Mock
    private MonitoredEndpointRepository endpointRepository;

    private HttpServer server;
    private int port;

    @BeforeEach
    void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void check_marksUpWhenServerReturns200() throws Exception {
        server.createContext("/ok", exchange -> {
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
        });
        server.start();

        MonitoredEndpoint endpoint = new MonitoredEndpoint();
        endpoint.setUrl("http://localhost:" + port + "/ok");

        EndpointCheckerService checker = new EndpointCheckerService(checkResultRepository, endpointRepository);
        checker.check(endpoint);

        ArgumentCaptor<CheckResult> captor = ArgumentCaptor.forClass(CheckResult.class);
        verify(checkResultRepository).save(captor.capture());

        assertThat(captor.getValue().isUp()).isTrue();
        assertThat(captor.getValue().getStatusCode()).isEqualTo(200);
    }

    @Test
    void check_marksDownWhenServerReturns500() throws Exception {
        server.createContext("/fail", exchange -> {
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        });
        server.start();

        MonitoredEndpoint endpoint = new MonitoredEndpoint();
        endpoint.setUrl("http://localhost:" + port + "/fail");

        EndpointCheckerService checker = new EndpointCheckerService(checkResultRepository, endpointRepository);
        checker.check(endpoint);

        ArgumentCaptor<CheckResult> captor = ArgumentCaptor.forClass(CheckResult.class);
        verify(checkResultRepository).save(captor.capture());

        assertThat(captor.getValue().isUp()).isFalse();
        assertThat(captor.getValue().getStatusCode()).isEqualTo(500);
    }

    @Test
    void check_marksDownWhenConnectionRefused() {
        MonitoredEndpoint endpoint = new MonitoredEndpoint();
        endpoint.setUrl("http://localhost:1");

        EndpointCheckerService checker = new EndpointCheckerService(checkResultRepository, endpointRepository);
        checker.check(endpoint);

        ArgumentCaptor<CheckResult> captor = ArgumentCaptor.forClass(CheckResult.class);
        verify(checkResultRepository).save(captor.capture());

        assertThat(captor.getValue().isUp()).isFalse();
        assertThat(captor.getValue().getErrorMessage()).isNotNull();
    }
}
