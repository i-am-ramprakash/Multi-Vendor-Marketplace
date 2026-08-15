package com.marketplace.notification.domain.event.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {

    private String eventId;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Instant occurredOn;
}