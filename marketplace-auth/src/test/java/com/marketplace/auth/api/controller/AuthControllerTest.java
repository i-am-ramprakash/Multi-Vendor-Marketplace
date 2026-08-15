package com.marketplace.auth.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.auth.application.dto.*;
import com.marketplace.auth.application.service.AuthService;
import com.marketplace.auth.api.advice.ErrorResponse;
import com.marketplace.auth.api.advice.GlobalExceptionHandler;
import com.marketplace.auth.domain.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

        authResponse = AuthResponse.builder()
            .accessToken("access-token")
            .refreshToken("refresh-token")
            .tokenType("Bearer")
            .expiresIn(3600L)
            .user(AuthResponse.UserInfo.builder()
                .id(1L)
                .publicId("550e8400-e29b-41d4-a716-446655440000")
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .fullName("John Doe")
                .emailVerified(true)
                .status("ACTIVE")
                .roles(Set.of("CUSTOMER"))
                .build())
            .build();
    }

    @Test
    void registerCustomer_WithValidRequest_ShouldReturn200() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
            .email("john@example.com")
            .password("SecurePass123!")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(authService.registerCustomer(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("access-token"))
            .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.user.email").value("john@example.com"));

        verify(authService).registerCustomer(any(RegisterRequest.class));
    }

    @Test
    void registerCustomer_WithInvalidEmail_ShouldReturn400() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
            .email("invalid-email")
            .password("SecurePass123!")
            .firstName("John")
            .lastName("Doe")
            .build();

        mockMvc.perform(post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(authService, never()).registerCustomer(any());
    }

    @Test
    void registerCustomer_WithDuplicateEmail_ShouldReturn409() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
            .email("existing@example.com")
            .password("SecurePass123!")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(authService.registerCustomer(any(RegisterRequest.class)))
            .thenThrow(new EmailAlreadyExistsException("existing@example.com"));

        mockMvc.perform(post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("EMAIL_ALREADY_EXISTS"));

        verify(authService).registerCustomer(any(RegisterRequest.class));
    }

    @Test
    void login_WithValidCredentials_ShouldReturn200() throws Exception {
        LoginRequest request = LoginRequest.builder()
            .email("john@example.com")
            .password("SecurePass123!")
            .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("access-token"))
            .andExpect(jsonPath("$.refreshToken").value("refresh-token"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturn401() throws Exception {
        LoginRequest request = LoginRequest.builder()
            .email("john@example.com")
            .password("WrongPass123!")
            .build();

        when(authService.login(any(LoginRequest.class)))
            .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void refreshToken_WithValidToken_ShouldReturn200() throws Exception {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
            .refreshToken("valid-refresh-token")
            .build();

        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("access-token"))
            .andExpect(jsonPath("$.refreshToken").value("refresh-token"));

        verify(authService).refreshToken(any(RefreshTokenRequest.class));
    }

    @Test
    void refreshToken_WithInvalidToken_ShouldReturn400() throws Exception {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
            .refreshToken("invalid-refresh-token")
            .build();

        when(authService.refreshToken(any(RefreshTokenRequest.class)))
            .thenThrow(TokenException.invalid());

        mockMvc.perform(post("/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("TOKEN_INVALID"));

        verify(authService).refreshToken(any(RefreshTokenRequest.class));
    }

    @Test
    void logout_WithValidToken_ShouldReturn200() throws Exception {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
            .refreshToken("valid-refresh-token")
            .build();

        doNothing().when(authService).logout(anyString());

        mockMvc.perform(post("/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Logged out successfully"))
            .andExpect(jsonPath("$.success").value(true));

        verify(authService).logout(anyString());
    }

    @Test
    void forgotPassword_WithValidEmail_ShouldReturn200() throws Exception {
        ForgotPasswordRequest request = ForgotPasswordRequest.builder()
            .email("john@example.com")
            .build();

        doNothing().when(authService).forgotPassword(any(ForgotPasswordRequest.class));

        mockMvc.perform(post("/v1/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(authService).forgotPassword(any(ForgotPasswordRequest.class));
    }

    @Test
    void resetPassword_WithValidToken_ShouldReturn200() throws Exception {
        ResetPasswordRequest request = ResetPasswordRequest.builder()
            .token("valid-reset-token")
            .newPassword("NewSecurePass123!")
            .confirmPassword("NewSecurePass123!")
            .build();

        doNothing().when(authService).resetPassword(any(ResetPasswordRequest.class));

        mockMvc.perform(post("/v1/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(authService).resetPassword(any(ResetPasswordRequest.class));
    }

    @Test
    void resetPassword_WithMismatchedPasswords_ShouldReturn400() throws Exception {
        ResetPasswordRequest request = ResetPasswordRequest.builder()
            .token("valid-reset-token")
            .newPassword("NewSecurePass123!")
            .confirmPassword("DifferentPass123!")
            .build();

        doNothing().when(authService).resetPassword(any(ResetPasswordRequest.class));

        mockMvc.perform(post("/v1/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(authService, never()).resetPassword(any());
    }

    @Test
    void verifyEmail_WithValidToken_ShouldReturn200() throws Exception {
        VerifyEmailRequest request = VerifyEmailRequest.builder()
            .token("valid-verification-token")
            .build();

        doNothing().when(authService).verifyEmail(any(VerifyEmailRequest.class));

        mockMvc.perform(post("/v1/auth/verify-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(authService).verifyEmail(any(VerifyEmailRequest.class));
    }

    @Test
    void forgotPassword_WithEmptyEmail_ShouldReturn400() throws Exception {
        ForgotPasswordRequest request = ForgotPasswordRequest.builder()
            .email("")
            .build();

        mockMvc.perform(post("/v1/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(authService, never()).forgotPassword(any());
    }

    @Test
    void changePassword_WithValidRequest_ShouldReturn200() throws Exception {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
            .currentPassword("OldPass123!")
            .newPassword("NewPass123!")
            .confirmPassword("NewPass123!")
            .build();

        doNothing().when(authService).changePassword(any(Long.class), any(ChangePasswordRequest.class));

        mockMvc.perform(post("/v1/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(authService).changePassword(any(Long.class), any(ChangePasswordRequest.class));
    }

    @Test
    void getProfile_ShouldReturn200() throws Exception {
        when(authService.getProfile(any(Long.class))).thenReturn(authResponse.getUser());

        mockMvc.perform(get("/v1/auth/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(authService).getProfile(any(Long.class));
    }
}