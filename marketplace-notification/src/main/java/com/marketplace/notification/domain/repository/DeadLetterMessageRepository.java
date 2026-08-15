package com.marketplace.notification.domain.repository;

import com.marketplace.notification.domain.entity.DeadLetterMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DeadLetterMessageRepository {

    DeadLetterMessage save(DeadLetterMessage deadLetterMessage);

    Optional<DeadLetterMessage> findById(Long id);

    List<DeadLetterMessage> findByNotificationId(Long notificationId);

    List<DeadLetterMessage> findByResolved(boolean resolved);

    Page<DeadLetterMessage> findByResolved(boolean resolved, Pageable pageable);

    long countByResolved(boolean resolved);
}