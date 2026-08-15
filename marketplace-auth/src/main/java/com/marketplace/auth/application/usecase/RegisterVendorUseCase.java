package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.VendorRegisterRequest;
import com.marketplace.auth.application.dto.AuthResponse;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.entity.Role;
import com.marketplace.auth.domain.event.UserRegisteredEvent;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.repository.RoleRepository;
import com.marketplace.auth.domain.service.PasswordService;
import com.marketplace.auth.domain.service.TokenService;
import com.marketplace.auth.domain.valueobject.Email;
import com.marketplace.auth.domain.valueobject.PasswordHash;
import com.marketplace.auth.domain.valueobject.PhoneNumber;
import com.marketplace.auth.domain.exception.EmailAlreadyExistsException;
import com.marketplace.auth.application.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterVendorUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AuthResponse execute(VendorRegisterRequest request) {
        Email email = Email.of(request.getEmail());
        
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email.getValue());
        }

        var validation = passwordService.validate(request.getPassword());
        if (!validation.valid()) {
            throw new ValidationException(validation.message());
        }

        Role vendorRole = roleRepository.findByName("VENDOR")
            .orElseThrow(() -> new IllegalStateException("VENDOR role not found"));
        Role customerRole = roleRepository.findByName("CUSTOMER")
            .orElseThrow(() -> new IllegalStateException("CUSTOMER role not found"));

        PasswordHash passwordHash = passwordService.hash(request.getPassword());
        PhoneNumber phone = request.getPhone() != null ? PhoneNumber.of(request.getPhone()) : PhoneNumber.empty();

        User user = new User(email, passwordHash, request.getFirstName(), request.getLastName());
        user.addRole(vendorRole);
        user.addRole(customerRole);
        if (phone.isPresent()) {
            user.setPhone(phone);
        }
        user.setStatus(com.marketplace.auth.domain.valueobject.UserStatus.PENDING_VERIFICATION);

        User savedUser = userRepository.save(user);

        String accessToken = tokenService.generateAccessToken(savedUser);
        String refreshToken = tokenService.generateRefreshToken(savedUser);
        Long expiresIn = java.time.Duration.between(java.time.Instant.now(), tokenService.getAccessTokenExpiry()).getSeconds();

        eventPublisher.publishEvent(new UserRegisteredEvent(
            savedUser.getId(), savedUser.getEmail(), savedUser.getFirstName(), savedUser.getLastName(), "VENDOR"
        ));

        return AuthResponse.of(accessToken, refreshToken, expiresIn, savedUser);
    }
}