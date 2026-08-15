package com.marketplace.auth.infrastructure.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.email")
@Data
public class EmailProperties {

    private String frontendUrl = "http://localhost:3000";
    private String fromEmail = "noreply@marketplace.example.com";
    private String fromName = "Multi-Vendor Marketplace";
}