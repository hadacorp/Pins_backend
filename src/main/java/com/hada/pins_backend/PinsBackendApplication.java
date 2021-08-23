package com.hada.pins_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PinsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinsBackendApplication.class, args);
    }
}
