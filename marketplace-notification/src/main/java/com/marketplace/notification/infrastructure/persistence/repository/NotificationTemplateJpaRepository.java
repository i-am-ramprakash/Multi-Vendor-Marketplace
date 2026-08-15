package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationType;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationTemplateJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplateJpaEntity, Long> {

    Optional<NotificationTemplateJpaEntity> findByCode(String code);

    List<NotificationTemplateJpaEntity> findByType(NotificationType type);

    List<NotificationTemplateJpaEntity> findByChannel(NotificationChannel channel);

    Optional<NotificationTemplateJpaEntity> findByTypeAndChannelAndLocaleAndIsActiveTrue(NotificationType type, NotificationChannel channel, String locale);

    List<NotificationTemplateJpaEntity> findByIsActiveTrue();

    Page<NotificationTemplateJpaEntity> findAll(Pageable pageable);

    long countByIsActiveTrue();
}