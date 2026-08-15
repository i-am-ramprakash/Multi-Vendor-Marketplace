package com.marketplace.notification.domain.repository;

import com.marketplace.notification.domain.entity.NotificationTemplate;
import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationTemplateRepository {

    NotificationTemplate save(NotificationTemplate template);

    Optional<NotificationTemplate> findById(Long id);

    Optional<NotificationTemplate> findByCode(String code);

    List<NotificationTemplate> findByType(NotificationType type);

    List<NotificationTemplate> findByChannel(NotificationChannel channel);

    Optional<NotificationTemplate> findByTypeAndChannelAndLocaleAndIsActiveTrue(NotificationType type, NotificationChannel channel, String locale);

    List<NotificationTemplate> findByIsActiveTrue();

    Page<NotificationTemplate> findAll(Pageable pageable);

    long countByIsActiveTrue();
}