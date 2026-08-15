package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.UpdateProfileRequest;
import com.marketplace.auth.application.dto.AuthResponse;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.valueobject.PhoneNumber;
import com.marketplace.auth.domain.exception.UserNotFoundException;
import com.marketplace.auth.application.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateProfileUseCase {

    private final UserRepository userRepository;

    @Transactional
    public AuthResponse.UserInfo execute(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        PhoneNumber phone = request.getPhone() != null && !request.getPhone().isBlank() 
            ? PhoneNumber.of(request.getPhone()) 
            : PhoneNumber.empty();

        user.updateProfile(
            request.getFirstName(),
            request.getLastName(),
            phone,
            request.getAvatarUrl()
        );

        User savedUser = userRepository.save(user);
        return AuthResponse.UserInfo.from(savedUser);
    }
}