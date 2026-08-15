package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.ChangePasswordRequest;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.repository.RefreshTokenRepository;
import com.marketplace.auth.domain.service.PasswordService;
import com.marketplace.auth.domain.service.TokenService;
import com.marketplace.auth.domain.valueobject.PasswordHash;
import com.marketplace.auth.domain.event.PasswordChangedEvent;
import com.marketplace.auth.domain.exception.UserNotFoundException;
import com.marketplace.auth.domain.exception.InvalidCredentialsException;
import com.marketplace.auth.application.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("New password and confirmation do not match");
        }

        var validation = passwordService.validate(request.getNewPassword());
        if (!validation.valid()) {
            throw new ValidationException(validation.message());
        }

        PasswordHash currentHash = com.marketplace.auth.domain.valueobject.PasswordHash.of(user.getPasswordHash());
        if (!passwordService.verify(request.getCurrentPassword(), currentHash)) {
            throw new InvalidCredentialsException();
        }

        PasswordHash newHash = passwordService.hash(request.getNewPassword());
        user.changePassword(newHash);
        userRepository.save(user);

        refreshTokenRepository.revokeAllByUser(user);

        eventPublisher.publishEvent(new PasswordChangedEvent(userId, true));
    }
}