package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.AuthResponse;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetProfileUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public AuthResponse.UserInfo execute(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        return AuthResponse.UserInfo.from(user);
    }
}