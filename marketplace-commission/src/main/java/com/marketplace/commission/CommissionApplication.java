package com.marketplace.commission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.marketplace.commission"})
@EntityScan(basePackages = {"com.marketplace.commission.infrastructure.persistence.entity"})
@EnableJpaRepositories(basePackages = {"com.marketplace.commission.infrastructure.persistence.repository"})
public class CommissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommissionApplication.class, args);
    }
}