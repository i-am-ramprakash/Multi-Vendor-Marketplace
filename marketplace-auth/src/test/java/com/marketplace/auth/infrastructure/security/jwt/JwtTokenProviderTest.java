package com.marketplace.auth.infrastructure.security.jwt;

import com.marketplace.auth.domain.entity.Role;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.valueobject.Email;
import com.marketplace.auth.domain.valueobject.PasswordHash;
import com.marketplace.auth.domain.valueobject.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private JwtProperties jwtProperties;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("my-secret-key-for-jwt-token-generation-must-be-at-least-256-bits-long");
        jwtProperties.setAccessTokenExpiryMinutes(60);
        jwtProperties.setRefreshTokenExpiryDays(30);
        jwtProperties.setIssuer("test-issuer");
        jwtProperties.setAudience("test-audience");

        tokenProvider = new JwtTokenProvider(jwtProperties);
        Method initMethod = JwtTokenProvider.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);
        initMethod.invoke(tokenProvider);

        Role customerRole = new Role("CUSTOMER", "Customer role");
        setId(customerRole, 1L);

        testUser = new User(
            Email.of("test@example.com"),
            PasswordHash.of("$2a$12$hashedpassword"),
            "John",
            "Doe"
        );
        setId(testUser, 1L);
        testUser.addRole(customerRole);
    }

    @Test
    void generateAccessToken_ShouldReturnValidToken() {
        String token = tokenProvider.generateAccessToken(testUser);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void generateRefreshToken_ShouldReturnValidToken() {
        String token = tokenProvider.generateRefreshToken(testUser);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void parseAccessToken_WithValidToken_ShouldReturnClaims() {
        String token = tokenProvider.generateAccessToken(testUser);

        var claims = tokenProvider.parseAccessToken(token);

        assertThat(claims).isNotNull();
        assertThat(claims.userId()).isEqualTo(1L);
        assertThat(claims.email()).isEqualTo("test@example.com");
        assertThat(claims.tokenType()).isEqualTo("ACCESS");
    }

    @Test
    void parseRefreshToken_WithValidToken_ShouldReturnClaims() {
        String token = tokenProvider.generateRefreshToken(testUser);

        var claims = tokenProvider.parseRefreshToken(token);

        assertThat(claims).isNotNull();
        assertThat(claims.userId()).isEqualTo(1L);
        assertThat(claims.tokenType()).isEqualTo("REFRESH");
    }

    @Test
    void validateAccessToken_WithValidToken_ShouldReturnOptional() {
        String token = tokenProvider.generateAccessToken(testUser);

        var result = tokenProvider.validateAccessToken(token);

        assertThat(result).isPresent();
        assertThat(result.get().userId()).isEqualTo(1L);
    }

    @Test
    void validateAccessToken_WithInvalidToken_ShouldReturnEmpty() {
        var result = tokenProvider.validateAccessToken("invalid-token");

        assertThat(result).isEmpty();
    }

    @Test
    void validateRefreshToken_WithValidToken_ShouldReturnOptional() {
        String token = tokenProvider.generateRefreshToken(testUser);

        var result = tokenProvider.validateRefreshToken(token);

        assertThat(result).isPresent();
        assertThat(result.get().userId()).isEqualTo(1L);
    }

    @Test
    void generateEmailVerificationToken_ShouldReturnValidToken() {
        String token = tokenProvider.generateEmailVerificationToken(testUser);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void parseEmailVerificationToken_WithValidToken_ShouldReturnUserId() {
        String token = tokenProvider.generateEmailVerificationToken(testUser);

        var result = tokenProvider.parseEmailVerificationToken(token);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(1L);
    }

    @Test
    void parseEmailVerificationToken_WithInvalidToken_ShouldReturnEmpty() {
        var result = tokenProvider.parseEmailVerificationToken("invalid-token");

        assertThat(result).isEmpty();
    }

    @Test
    void generatePasswordResetToken_ShouldReturnValidToken() {
        String token = tokenProvider.generatePasswordResetToken(testUser);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void parsePasswordResetToken_WithValidToken_ShouldReturnUserId() {
        String token = tokenProvider.generatePasswordResetToken(testUser);

        var result = tokenProvider.parsePasswordResetToken(token);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(1L);
    }

    @Test
    void parsePasswordResetToken_WithInvalidToken_ShouldReturnEmpty() {
        var result = tokenProvider.parsePasswordResetToken("invalid-token");

        assertThat(result).isEmpty();
    }

    @Test
    void getAccessTokenExpiry_ShouldReturnFutureTime() {
        Instant expiry = tokenProvider.getAccessTokenExpiry();

        assertThat(expiry).isAfter(Instant.now());
    }

    @Test
    void getRefreshTokenExpiry_ShouldReturnFutureTime() {
        Instant expiry = tokenProvider.getRefreshTokenExpiry();

        assertThat(expiry).isAfter(Instant.now());
    }

    @Test
    void parseAccessToken_WithAccessTokenUsedAsRefresh_ShouldThrowException() {
        String accessToken = tokenProvider.generateAccessToken(testUser);

        assertThatThrownBy(() -> tokenProvider.parseRefreshToken(accessToken))
            .isInstanceOf(io.jsonwebtoken.JwtException.class);
    }

    @Test
    void parseRefreshToken_WithRefreshTokenUsedAsAccess_ShouldThrowException() {
        String refreshToken = tokenProvider.generateRefreshToken(testUser);

        assertThatThrownBy(() -> tokenProvider.parseAccessToken(refreshToken))
            .isInstanceOf(io.jsonwebtoken.JwtException.class);
    }

    private void setId(User user, Long id) {
        try {
            java.lang.reflect.Field field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set user ID", e);
        }
    }

    private void setId(Role role, Long id) {
        try {
            java.lang.reflect.Field field = Role.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(role, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set role ID", e);
        }
    }
}