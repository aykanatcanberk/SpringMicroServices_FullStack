package com.fitness.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class UserserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);
    }

}
