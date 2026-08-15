package com.marketplace.product.domain.repository;

import com.marketplace.product.domain.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Category> findAll();

    List<Category> findByParentIsNull();

    List<Category> findByParentIsNullOrderByDisplayOrderAsc();

    List<Category> findByParentId(Long parentId);

    List<Category> findByParentIdOrderByDisplayOrderAsc(Long parentId);

    List<Category> findActiveCategories();

    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();

    void delete(Category category);
}