package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.ForgotPasswordRequest;
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
public class ForgotPasswordUseCase {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Transactional
    public void execute(ForgotPasswordRequest request) {
        Email email = Email.of(request.getEmail());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email.getValue()));

        String resetToken = tokenService.generatePasswordResetToken(user);
        
        // TODO: Send email with reset token
        // emailService.sendPasswordResetEmail(user.getEmail().getValue(), resetToken);
    }
}