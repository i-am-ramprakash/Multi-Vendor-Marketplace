package com.marketplace.product.infrastructure.persistence.mapper;

import com.marketplace.product.domain.entity.Category;
import com.marketplace.product.infrastructure.persistence.entity.CategoryJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CategoryPersistenceMapper {

    private CategoryPersistenceMapper() {}

    public static CategoryJpaEntity toJpaEntity(Category domain) {
        if (domain == null) return null;

        CategoryJpaEntity jpa = new CategoryJpaEntity();
        jpa.setId(domain.getId());
        jpa.setName(domain.getName());
        jpa.setSlug(domain.getSlug());
        jpa.setDescription(domain.getDescription());
        jpa.setImageUrl(domain.getImageUrl());
        jpa.setDisplayOrder(domain.getDisplayOrder());
        jpa.setIsActive(domain.getIsActive());
        jpa.setProductCount(domain.getProductCount());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getParent() != null) {
            CategoryJpaEntity parentJpa = new CategoryJpaEntity();
            parentJpa.setId(domain.getParent().getId());
            jpa.setParent(parentJpa);
        }

        return jpa;
    }

    public static Category toDomain(CategoryJpaEntity jpa) {
        if (jpa == null) return null;

        Category category = new Category(jpa.getName(), jpa.getSlug());
        setId(category, jpa.getId());
        category.setDescription(jpa.getDescription());
        category.setImageUrl(jpa.getImageUrl());
        category.setDisplayOrder(jpa.getDisplayOrder());
        category.setIsActive(jpa.getIsActive());
        category.setProductCount(jpa.getProductCount());
        category.setCreatedAt(jpa.getCreatedAt());
        category.setUpdatedAt(jpa.getUpdatedAt());

        if (jpa.getParent() != null) {
            Category parent = new Category(jpa.getParent().getName(), jpa.getParent().getSlug());
            setId(parent, jpa.getParent().getId());
            category.setParent(parent);
        }

        return category;
    }

    public static List<Category> toDomainList(List<CategoryJpaEntity> jpaList) {
        if (jpaList == null) return Collections.emptyList();
        List<Category> domains = new ArrayList<>();
        for (CategoryJpaEntity jpa : jpaList) {
            domains.add(toDomain(jpa));
        }
        return domains;
    }

    private static void setId(Category category, Long id) {
        try {
            Field field = Category.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(category, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set category ID", e);
        }
    }
}