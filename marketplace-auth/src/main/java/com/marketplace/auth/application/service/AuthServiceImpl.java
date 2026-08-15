package com.marketplace.auth.application.service;

import com.marketplace.auth.application.dto.*;
import com.marketplace.auth.application.usecase.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final RegisterVendorUseCase registerVendorUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final GetProfileUseCase getProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final ResendVerificationEmailUseCase resendVerificationEmailUseCase;

    @Override
    @Transactional
    public AuthResponse registerCustomer(RegisterRequest request) {
        return registerCustomerUseCase.execute(request);
    }

    @Override
    @Transactional
    public AuthResponse registerVendor(VendorRegisterRequest request) {
        return registerVendorUseCase.execute(request);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        return loginUseCase.execute(request, getClientIp(), getUserAgent());
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenUseCase.execute(request);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        logoutUseCase.execute(refreshToken);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        changePasswordUseCase.execute(userId, request);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        forgotPasswordUseCase.execute(request);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request);
    }

    @Override
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        verifyEmailUseCase.execute(request);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse.UserInfo getProfile(Long userId) {
        return getProfileUseCase.execute(userId);
    }

    @Override
    @Transactional
    public AuthResponse.UserInfo updateProfile(Long userId, UpdateProfileRequest request) {
        return updateProfileUseCase.execute(userId, request);
    }

    @Override
    @Transactional
    public void resendVerificationEmail(String email) {
        resendVerificationEmailUseCase.execute(email);
    }

    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }
            String xRealIp = request.getHeader("X-Real-IP");
            if (xRealIp != null && !xRealIp.isEmpty()) {
                return xRealIp;
            }
            return request.getRemoteAddr();
        }
        return "unknown";
    }

    private String getUserAgent() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            String userAgent = attrs.getRequest().getHeader("User-Agent");
            return userAgent != null ? userAgent : "unknown";
        }
        return "unknown";
    }
}