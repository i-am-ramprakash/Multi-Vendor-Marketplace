package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.domain.entity.Category;
import com.marketplace.product.domain.repository.CategoryRepository;
import com.marketplace.product.infrastructure.persistence.entity.CategoryJpaEntity;
import com.marketplace.product.infrastructure.persistence.mapper.CategoryPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;

    @Override
    public Category save(Category category) {
        CategoryJpaEntity jpa = CategoryPersistenceMapper.toJpaEntity(category);
        CategoryJpaEntity saved = jpaRepository.save(jpa);
        return CategoryPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id)
            .map(CategoryPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug)
            .map(CategoryPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }

    @Override
    public List<Category> findAll() {
        return CategoryPersistenceMapper.toDomainList(jpaRepository.findAll());
    }

    @Override
    public List<Category> findByParentIsNull() {
        return CategoryPersistenceMapper.toDomainList(jpaRepository.findByParentIsNull());
    }

    @Override
    public List<Category> findByParentIsNullOrderByDisplayOrderAsc() {
        return CategoryPersistenceMapper.toDomainList(jpaRepository.findByParentIsNullOrderByDisplayOrderAsc());
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        return CategoryPersistenceMapper.toDomainList(jpaRepository.findByParentId(parentId));
    }

    @Override
    public List<Category> findByParentIdOrderByDisplayOrderAsc(Long parentId) {
        return CategoryPersistenceMapper.toDomainList(jpaRepository.findByParentIdOrderByDisplayOrderAsc(parentId));
    }

    @Override
    public List<Category> findActiveCategories() {
        return CategoryPersistenceMapper.toDomainList(jpaRepository.findByIsActiveTrueOrderByDisplayOrderAsc());
    }

    @Override
    public List<Category> findByIsActiveTrueOrderByDisplayOrderAsc() {
        return CategoryPersistenceMapper.toDomainList(jpaRepository.findByIsActiveTrueOrderByDisplayOrderAsc());
    }

    @Override
    public void delete(Category category) {
        jpaRepository.deleteById(category.getId());
    }
}