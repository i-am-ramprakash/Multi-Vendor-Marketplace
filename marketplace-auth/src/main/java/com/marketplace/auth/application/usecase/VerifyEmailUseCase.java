package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.VerifyEmailRequest;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.service.TokenService;
import com.marketplace.auth.domain.event.EmailVerifiedEvent;
import com.marketplace.auth.domain.exception.UserNotFoundException;
import com.marketplace.auth.domain.exception.TokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(VerifyEmailRequest request) {
        Long userId = tokenService.parseEmailVerificationToken(request.getToken())
            .orElseThrow(() -> TokenException.invalid());

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        user.verifyEmail();
        user.activate();
        userRepository.save(user);

        eventPublisher.publishEvent(new EmailVerifiedEvent(userId, user.getEmail().getValue()));
    }
}