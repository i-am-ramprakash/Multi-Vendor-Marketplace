package com.marketplace.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.marketplace.urlshortener"})
@EntityScan(basePackages = {"com.marketplace.urlshortener.infrastructure.persistence.entity"})
@EnableJpaRepositories(basePackages = {"com.marketplace.urlshortener.infrastructure.persistence.repository"})
public class UrlShortenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }
}