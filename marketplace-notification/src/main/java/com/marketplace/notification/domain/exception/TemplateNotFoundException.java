package com.marketplace.notification.domain.exception;

public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException(Long id) {
        super("Template not found with id: " + id);
    }

    public TemplateNotFoundException(String code) {
        super("Template not found with code: " + code);
    }
}