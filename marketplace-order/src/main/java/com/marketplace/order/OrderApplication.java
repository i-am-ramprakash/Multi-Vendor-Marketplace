package com.marketplace.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
    "com.marketplace.order.infrastructure.persistence.entity",
    "com.marketplace.auth.infrastructure.persistence.entity"
})
@ComponentScan(basePackages = {
    "com.marketplace.order",
    "com.marketplace.auth.infrastructure.security"
})
@EnableJpaRepositories(basePackages = {
    "com.marketplace.order.infrastructure.persistence.repository",
    "com.marketplace.auth.infrastructure.persistence.repository"
})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}