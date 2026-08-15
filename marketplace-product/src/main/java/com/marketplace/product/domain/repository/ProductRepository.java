package com.marketplace.product.domain.repository;

import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.valueobject.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Product> findAll();

    List<Product> findByVendorId(Long vendorId);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByVendorIdAndStatus(Long vendorId, ProductStatus status);

    Page<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> searchByNameContaining(String keyword, Pageable pageable);

    Page<Product> search(String keyword, Long categoryId, Long vendorId, 
                        ProductStatus status, Pageable pageable);

    long countByVendorId(Long vendorId);

    long countByCategoryId(Long categoryId);

    long countByStatus(ProductStatus status);

    void delete(Product product);
}