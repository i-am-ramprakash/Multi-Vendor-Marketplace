package com.marketplace.auth.application.usecase;

import com.marketplace.auth.application.dto.RefreshTokenRequest;
import com.marketplace.auth.application.dto.AuthResponse;
import com.marketplace.auth.domain.entity.User;
import com.marketplace.auth.domain.entity.RefreshToken;
import com.marketplace.auth.domain.repository.UserRepository;
import com.marketplace.auth.domain.repository.RefreshTokenRepository;
import com.marketplace.auth.domain.service.TokenService;
import com.marketplace.auth.domain.exception.UserNotFoundException;
import com.marketplace.auth.domain.exception.TokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    @Transactional
    public AuthResponse execute(RefreshTokenRequest request) {
        TokenService.TokenClaims claims = tokenService.parseRefreshToken(request.getRefreshToken());
        
        User user = userRepository.findById(claims.userId())
            .orElseThrow(() -> new UserNotFoundException(claims.userId()));

        if (!user.getStatus().equals(com.marketplace.auth.domain.valueobject.UserStatus.ACTIVE)) {
            throw new com.marketplace.auth.domain.exception.UserNotActiveException(user.getStatus().name());
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
            .orElseThrow(() -> TokenException.invalid());

        if (!storedToken.isValid()) {
            if (storedToken.isExpired()) {
                throw TokenException.expired();
            }
            throw TokenException.revoked();
        }

        storedToken.revoke();
        refreshTokenRepository.save(storedToken);

        String newAccessToken = tokenService.generateAccessToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);
        Long expiresIn = java.time.Duration.between(java.time.Instant.now(), tokenService.getAccessTokenExpiry()).getSeconds();

        RefreshToken newRefreshTokenEntity = new RefreshToken(user, newRefreshToken, tokenService.getRefreshTokenExpiry());
        refreshTokenRepository.save(newRefreshTokenEntity);

        return AuthResponse.of(newAccessToken, newRefreshToken, expiresIn, user);
    }
}