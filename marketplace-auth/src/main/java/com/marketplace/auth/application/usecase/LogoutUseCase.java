package com.marketplace.auth.application.usecase;

import com.marketplace.auth.domain.entity.RefreshToken;
import com.marketplace.auth.domain.repository.RefreshTokenRepository;
import com.marketplace.auth.domain.service.TokenService;
import com.marketplace.auth.domain.exception.TokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    @Transactional
    public void execute(String refreshToken) {
        TokenService.TokenClaims claims = tokenService.parseRefreshToken(refreshToken);
        
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> TokenException.invalid());

        storedToken.revoke();
        refreshTokenRepository.save(storedToken);
    }
}