package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.domain.entity.NotificationTemplate;
import com.marketplace.notification.domain.repository.NotificationTemplateRepository;
import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationType;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationTemplateJpaEntity;
import com.marketplace.notification.infrastructure.persistence.mapper.NotificationTemplatePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationTemplateRepositoryImpl implements NotificationTemplateRepository {

    private final NotificationTemplateJpaRepository jpaRepository;

    @Override
    public NotificationTemplate save(NotificationTemplate template) {
        NotificationTemplateJpaEntity jpa = NotificationTemplatePersistenceMapper.toJpaEntity(template);
        NotificationTemplateJpaEntity saved = jpaRepository.save(jpa);
        return NotificationTemplatePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<NotificationTemplate> findById(Long id) {
        return jpaRepository.findById(id)
            .map(NotificationTemplatePersistenceMapper::toDomain);
    }

    @Override
    public Optional<NotificationTemplate> findByCode(String code) {
        return jpaRepository.findByCode(code)
            .map(NotificationTemplatePersistenceMapper::toDomain);
    }

    @Override
    public List<NotificationTemplate> findByType(NotificationType type) {
        return jpaRepository.findByType(type).stream()
            .map(NotificationTemplatePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationTemplate> findByChannel(NotificationChannel channel) {
        return jpaRepository.findByChannel(channel).stream()
            .map(NotificationTemplatePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<NotificationTemplate> findByTypeAndChannelAndLocaleAndIsActiveTrue(NotificationType type, NotificationChannel channel, String locale) {
        return jpaRepository.findByTypeAndChannelAndLocaleAndIsActiveTrue(type, channel, locale)
            .map(NotificationTemplatePersistenceMapper::toDomain);
    }

    @Override
    public List<NotificationTemplate> findByIsActiveTrue() {
        return jpaRepository.findByIsActiveTrue().stream()
            .map(NotificationTemplatePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<NotificationTemplate> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
            .map(NotificationTemplatePersistenceMapper::toDomain);
    }

    @Override
    public long countByIsActiveTrue() {
        return jpaRepository.countByIsActiveTrue();
    }
}