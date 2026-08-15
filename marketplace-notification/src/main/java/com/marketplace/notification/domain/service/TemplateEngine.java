package com.marketplace.notification.domain.service;

public interface TemplateEngine {

    String render(String template, java.util.Map<String, String> variables);

    String renderWithDefaults(String template, java.util.Map<String, String> variables);
}