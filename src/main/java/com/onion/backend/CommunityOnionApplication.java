package com.onion.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CommunityOnionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityOnionApplication.class, args);
    }

}
