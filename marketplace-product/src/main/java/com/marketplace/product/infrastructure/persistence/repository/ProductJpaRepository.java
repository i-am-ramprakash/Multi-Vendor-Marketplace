package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity;
import com.marketplace.product.domain.valueobject.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    Optional<ProductJpaEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<ProductJpaEntity> findByVendorId(Long vendorId);

    List<ProductJpaEntity> findByCategoryId(Long categoryId);

    List<ProductJpaEntity> findByStatus(ProductStatus status);

    List<ProductJpaEntity> findByVendorIdAndStatus(Long vendorId, ProductStatus status);

    Page<ProductJpaEntity> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);

    Page<ProductJpaEntity> findByStatus(ProductStatus status, Pageable pageable);

    @Query("SELECT p FROM ProductJpaEntity p WHERE " +
           "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:vendorId IS NULL OR p.vendorId = :vendorId) AND " +
           "(:status IS NULL OR p.status = :status)")
    Page<ProductJpaEntity> search(
        @Param("keyword") String keyword,
        @Param("categoryId") Long categoryId,
        @Param("vendorId") Long vendorId,
        @Param("status") ProductStatus status,
        Pageable pageable
    );

    long countByVendorId(Long vendorId);

    long countByCategoryId(Long categoryId);

    long countByStatus(ProductStatus status);
}