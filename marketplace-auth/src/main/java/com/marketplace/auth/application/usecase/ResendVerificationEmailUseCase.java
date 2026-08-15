package com.marketplace.auth.application.usecase;

import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.service.TokenService;
import com.marketplace.auth.domain.valueobject.Email;
import com.marketplace.auth.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResendVerificationEmailUseCase {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Transactional
    public void execute(String email) {
        Email emailVO = Email.of(email);
        User user = userRepository.findByEmail(emailVO)
            .orElseThrow(() -> new UserNotFoundException(email));

        if (user.isEmailVerified()) {
            return; // Already verified, silently succeed
        }

        String verificationToken = tokenService.generateEmailVerificationToken(user);
        
        // TODO: Send email with verification token
        // emailService.sendVerificationEmail(user.getEmail().getValue(), verificationToken);
    }
}