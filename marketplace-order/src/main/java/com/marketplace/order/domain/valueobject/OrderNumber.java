package com.marketplace.order.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@EqualsAndHashCode
public class OrderNumber {

    private final String value;

    private OrderNumber(String value) {
        this.value = value;
    }

    public static OrderNumber generate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            .withZone(ZoneId.systemDefault());
        String datePart = formatter.format(Instant.now());
        long randomPart = ThreadLocalRandom.current().nextLong(100000, 999999);
        return new OrderNumber("ORD-" + datePart + "-" + randomPart);
    }

    public static OrderNumber of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Order number cannot be null or blank");
        }
        return new OrderNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}