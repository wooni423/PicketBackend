package com.swyg.picketbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PicketBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicketBackendApplication.class, args);
    }

}
