package com.marketplace.auth.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    private Long id;
    private String name;
    private String description;
    private Instant createdAt;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Role admin() {
        return new Role("ADMIN", "System administrator with full access");
    }

    public static Role vendor() {
        return new Role("VENDOR", "Vendor who can manage products and orders");
    }

    public static Role customer() {
        return new Role("CUSTOMER", "Regular customer who can purchase products");
    }
}