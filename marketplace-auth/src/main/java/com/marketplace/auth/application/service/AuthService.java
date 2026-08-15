package com.marketplace.auth.application.service;

import com.marketplace.auth.application.dto.*;

public interface AuthService {

    AuthResponse registerCustomer(RegisterRequest request);

    AuthResponse registerVendor(VendorRegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String refreshToken);

    void changePassword(Long userId, ChangePasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void verifyEmail(VerifyEmailRequest request);

    AuthResponse.UserInfo getProfile(Long userId);

    AuthResponse.UserInfo updateProfile(Long userId, UpdateProfileRequest request);

    void resendVerificationEmail(String email);
}