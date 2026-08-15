package com.marketplace.admin.infrastructure.persistence.repository;

import com.marketplace.admin.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    @Query("SELECT COUNT(u) FROM UserJpaEntity u")
    Long countAllUsers();

    @Query("SELECT COUNT(u) FROM UserJpaEntity u WHERE u.role = :role")
    Long countByRole(@Param("role") String role);

    @Query("SELECT COUNT(u) FROM UserJpaEntity u WHERE u.createdAt BETWEEN :from AND :to")
    Long countByCreatedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COUNT(u) FROM UserJpaEntity u WHERE u.enabled = true")
    Long countActiveUsers();
}