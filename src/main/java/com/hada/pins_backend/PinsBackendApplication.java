package com.hada.pins_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ServletComponentScan
public class PinsBackendApplication {

    // 2개 yml 파일을 같이 사용할때
//    public static final String APPLICATION_LOCATIONS = "spring.config.location="
//            + "classpath:application.yml,"
//            + "classpath:aws.yml";
//
//    public static void main(String[] args) {
//        new SpringApplicationBuilder(PinsBackendApplication.class)
//                .properties(APPLICATION_LOCATIONS)
//                .run(args);
//    }
    public static void main(String[] args) {
        SpringApplication.run(PinsBackendApplication.class, args);
    }
}
