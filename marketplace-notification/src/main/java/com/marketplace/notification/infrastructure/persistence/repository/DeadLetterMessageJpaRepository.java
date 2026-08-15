package com.marketplace.notification.infrastructure.persistence.repository;

import com.marketplace.notification.infrastructure.persistence.entity.DeadLetterMessageJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeadLetterMessageJpaRepository extends JpaRepository<DeadLetterMessageJpaEntity, Long> {

    List<DeadLetterMessageJpaEntity> findByNotificationId(Long notificationId);

    List<DeadLetterMessageJpaEntity> findByResolved(boolean resolved);

    Page<DeadLetterMessageJpaEntity> findByResolved(boolean resolved, Pageable pageable);

    long countByResolved(boolean resolved);
}