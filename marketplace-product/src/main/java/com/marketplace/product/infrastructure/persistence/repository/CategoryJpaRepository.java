package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.infrastructure.persistence.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {

    Optional<CategoryJpaEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<CategoryJpaEntity> findByParentIsNull();

    List<CategoryJpaEntity> findByParentIsNullOrderByDisplayOrderAsc();

    List<CategoryJpaEntity> findByParentId(Long parentId);

    List<CategoryJpaEntity> findByParentIdOrderByDisplayOrderAsc(Long parentId);

    List<CategoryJpaEntity> findByIsActiveTrueOrderByDisplayOrderAsc();
}