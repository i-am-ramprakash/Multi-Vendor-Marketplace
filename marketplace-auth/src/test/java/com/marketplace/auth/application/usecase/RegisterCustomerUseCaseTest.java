package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.AuthResponse;
import com.marketplace.auth.application.dto.RegisterRequest;
import com.marketplace.auth.domain.entity.Role;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.event.UserRegisteredEvent;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.repository.RoleRepository;
import com.marketplace.auth.domain.service.PasswordService;
import com.marketplace.auth.domain.service.TokenService;
import com.marketplace.auth.domain.valueobject.Email;
import com.marketplace.auth.domain.valueobject.PasswordHash;
import com.marketplace.auth.domain.exception.EmailAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterCustomerUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private TokenService tokenService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private RegisterCustomerUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterCustomerUseCase(
            userRepository, roleRepository, passwordService, tokenService, eventPublisher
        );
    }

    @Test
    void execute_WithValidRequest_ShouldRegisterUserAndReturnTokens() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
            .email("john@example.com")
            .password("SecurePass123!")
            .firstName("John")
            .lastName("Doe")
            .phone("+1234567890")
            .build();

        Role customerRole = new Role("CUSTOMER", "Customer role");
        setId(customerRole, 1L);

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(passwordService.validate(request.getPassword())).thenReturn(PasswordService.ValidationResult.createValid());
        when(passwordService.hash(request.getPassword())).thenReturn(PasswordHash.of("$2a$12$hashedpassword"));
        when(tokenService.generateAccessToken(any(User.class))).thenReturn("access-token");
        when(tokenService.generateRefreshToken(any(User.class))).thenReturn("refresh-token");
        when(tokenService.getAccessTokenExpiry()).thenReturn(java.time.Instant.now().plusSeconds(3600));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        AuthResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getEmail()).isEqualTo("john@example.com");
        assertThat(response.getUser().getFirstName()).isEqualTo("John");
        assertThat(response.getUser().getLastName()).isEqualTo("Doe");
        assertThat(response.getUser().getRoles()).contains("CUSTOMER");

        verify(userRepository).save(any(User.class));
        verify(eventPublisher).publishEvent(any(UserRegisteredEvent.class));
    }

    @Test
    void execute_WithExistingEmail_ShouldThrowEmailAlreadyExistsException() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
            .email("john@example.com")
            .password("SecurePass123!")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessageContaining("john@example.com");

        verify(userRepository, never()).save(any(User.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void execute_WithWeakPassword_ShouldThrowValidationException() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
            .email("john@example.com")
            .password("weak")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(passwordService.validate(request.getPassword()))
            .thenReturn(PasswordService.ValidationResult.invalid("Password is too weak"));

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(com.marketplace.auth.application.exception.ValidationException.class)
            .hasMessageContaining("too weak");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void execute_WithoutPhone_ShouldRegisterUserWithoutPhone() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
            .email("john@example.com")
            .password("SecurePass123!")
            .firstName("John")
            .lastName("Doe")
            .build();

        Role customerRole = new Role("CUSTOMER", "Customer role");
        setId(customerRole, 1L);

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(passwordService.validate(request.getPassword())).thenReturn(PasswordService.ValidationResult.createValid());
        when(passwordService.hash(request.getPassword())).thenReturn(PasswordHash.of("$2a$12$hashedpassword"));
        when(tokenService.generateAccessToken(any(User.class))).thenReturn("access-token");
        when(tokenService.generateRefreshToken(any(User.class))).thenReturn("refresh-token");
        when(tokenService.getAccessTokenExpiry()).thenReturn(java.time.Instant.now().plusSeconds(3600));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        AuthResponse response = useCase.execute(request);

        // Then
        assertThat(response.getUser().getPhone()).isNull();
        verify(userRepository).save(any(User.class));
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