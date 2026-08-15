package com.marketplace.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
    "com.marketplace.product.infrastructure.persistence.entity",
    "com.marketplace.auth.infrastructure.persistence.entity"
})
@ComponentScan(basePackages = {
    "com.marketplace.product",
    "com.marketplace.auth.infrastructure.persistence",
    "com.marketplace.auth.infrastructure.security.jwt"
})
@EnableJpaRepositories(basePackages = {
    "com.marketplace.product.infrastructure.persistence.repository",
    "com.marketplace.auth.infrastructure.persistence.repository"
})
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}