package com.marketplace.auth.infrastructure.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = emailProperties.getFrontendUrl() + "/verify-email?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your email address");
        message.setText("""
            Welcome to Multi-Vendor Marketplace!
            
            Please click the link below to verify your email address:
            %s
            
            This link will expire in 24 hours.
            
            If you didn't create an account, please ignore this email.
            """.formatted(verificationUrl));
        
        mailSender.send(message);
        log.info("Verification email sent to: {}", to);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = emailProperties.getFrontendUrl() + "/reset-password?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset your password");
        message.setText("""
            You requested a password reset for your Multi-Vendor Marketplace account.
            
            Please click the link below to reset your password:
            %s
            
            This link will expire in 1 hour.
            
            If you didn't request this, please ignore this email.
            """.formatted(resetUrl));
        
        mailSender.send(message);
        log.info("Password reset email sent to: {}", to);
    }

    public void sendWelcomeEmail(String to, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Multi-Vendor Marketplace!");
        message.setText("""
            Hi %s,
            
            Welcome to Multi-Vendor Marketplace! Your account has been successfully created.
            
            You can now:
            - Browse and purchase products from multiple vendors
            - Manage your orders and wishlist
            - Track your shipments
            
            If you're a vendor, you can start setting up your store after approval.
            
            Happy shopping!
            
            The Multi-Vendor Marketplace Team
            """.formatted(firstName));
        
        mailSender.send(message);
        log.info("Welcome email sent to: {}", to);
    }

    public void sendVendorApprovalEmail(String to, String storeName, boolean approved) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        
        if (approved) {
            message.setSubject("Your vendor application has been approved!");
            message.setText("""
                Congratulations! Your vendor application for "%s" has been approved.
                
                You can now:
                - Set up your store profile
                - Add products and manage inventory
                - Process orders and manage fulfillment
                - View analytics and sales reports
                
                Log in to your vendor dashboard to get started.
                
                The Multi-Vendor Marketplace Team
                """.formatted(storeName));
        } else {
            message.setSubject("Your vendor application requires attention");
            message.setText("""
                Thank you for applying to become a vendor on Multi-Vendor Marketplace.
                
                Unfortunately, your application for "%s" requires additional information or has not been approved at this time.
                
                Please contact our support team for more details or to resubmit your application.
                
                The Multi-Vendor Marketplace Team
                """.formatted(storeName));
        }
        
        mailSender.send(message);
        log.info("Vendor approval email sent to: {}", to);
    }
}