package com.marketplace.notification.domain.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);

    void sendHtmlEmail(String to, String subject, String htmlBody);

    void sendEmailWithAttachment(String to, String subject, String body, String attachmentName, byte[] attachment);
}