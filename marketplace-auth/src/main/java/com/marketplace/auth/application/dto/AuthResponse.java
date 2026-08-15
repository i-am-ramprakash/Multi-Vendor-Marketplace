package com.marketplace.auth.application.dto;

import com.marketplace.auth.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response with tokens")
public class AuthResponse {

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", description = "JWT access token")
    private String accessToken;

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", description = "JWT refresh token")
    private String refreshToken;

    @Schema(example = "Bearer", description = "Token type")
    private String tokenType;

    @Schema(example = "3600", description = "Access token expiry in seconds")
    private Long expiresIn;

    @Schema(description = "User information")
    private UserInfo user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "User information")
    public static class UserInfo {
        @Schema(example = "1")
        private Long id;

        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        private String publicId;

        @Schema(example = "john.doe@example.com")
        private String email;

        @Schema(example = "John")
        private String firstName;

        @Schema(example = "Doe")
        private String lastName;

        @Schema(example = "John Doe")
        private String fullName;

        @Schema(example = "+1234567890")
        private String phone;

        @Schema(example = "https://example.com/avatar.jpg")
        private String avatarUrl;

        @Schema(example = "true")
        private Boolean emailVerified;

        @Schema(example = "ACTIVE")
        private String status;

        @Schema(example = "[\"CUSTOMER\"]")
        private Set<String> roles;

        public static UserInfo from(User user) {
            return UserInfo.builder()
                .id(user.getId())
                .publicId(user.getPublicId())
                .email(user.getEmail().getValue())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .phone(user.getPhone() != null ? user.getPhone().getValue() : null)
                .avatarUrl(user.getAvatarUrl())
                .emailVerified(user.isEmailVerified())
                .status(user.getStatus().name())
                .roles(user.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet()))
                .build();
        }
    }

    public static AuthResponse of(String accessToken, String refreshToken, Long accessTokenExpirySeconds, User user) {
        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(accessTokenExpirySeconds)
            .user(UserInfo.from(user))
            .build();
    }
}