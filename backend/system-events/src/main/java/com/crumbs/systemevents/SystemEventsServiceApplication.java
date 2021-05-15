package com.crumbs.systemevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SystemEventsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemEventsServiceApplication.class, args);
    }
}
