package com.codealpha.endpointmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling

@SpringBootApplication
public class EndpointMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EndpointMonitorApplication.class, args);
	}

}
