package com.marketplace.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.marketplace.admin")
@EntityScan(basePackages = "com.marketplace.admin.infrastructure.persistence.entity")
@EnableJpaRepositories(basePackages = "com.marketplace.admin.infrastructure.persistence.repository")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}