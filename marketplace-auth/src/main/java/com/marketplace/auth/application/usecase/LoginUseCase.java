package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.LoginRequest;
import com.marketplace.auth.application.dto.AuthResponse;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.entity.RefreshToken;
import com.marketplace.auth.domain.event.UserLoggedInEvent;
import com.marketplace.auth.domain.valueobject.PasswordHash;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.repository.RefreshTokenRepository;
import com.marketplace.auth.domain.service.PasswordService;
import com.marketplace.auth.domain.service.TokenService;
import com.marketplace.auth.domain.valueobject.Email;
import com.marketplace.auth.domain.exception.UserNotFoundException;
import com.marketplace.auth.domain.exception.InvalidCredentialsException;
import com.marketplace.auth.domain.exception.UserNotActiveException;
import com.marketplace.auth.domain.exception.EmailNotVerifiedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AuthResponse execute(LoginRequest request, String ipAddress, String userAgent) {
        Email email = Email.of(request.getEmail());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException());

        if (!user.getStatus().equals(com.marketplace.auth.domain.valueobject.UserStatus.ACTIVE)) {
            throw new UserNotActiveException(user.getStatus().name());
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException();
        }

        PasswordHash storedHash = com.marketplace.auth.domain.valueobject.PasswordHash.of(user.getPasswordHash());
        if (!passwordService.verify(request.getPassword(), storedHash)) {
            throw new InvalidCredentialsException();
        }

        user.recordLogin();
        userRepository.save(user);

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
        Long expiresIn = java.time.Duration.between(java.time.Instant.now(), tokenService.getAccessTokenExpiry()).getSeconds();

        RefreshToken refreshTokenEntity = new RefreshToken(user, refreshToken, tokenService.getRefreshTokenExpiry());
        refreshTokenRepository.save(refreshTokenEntity);

        eventPublisher.publishEvent(new UserLoggedInEvent(user.getId(), ipAddress, userAgent));

        return AuthResponse.of(accessToken, refreshToken, expiresIn, user);
    }
}