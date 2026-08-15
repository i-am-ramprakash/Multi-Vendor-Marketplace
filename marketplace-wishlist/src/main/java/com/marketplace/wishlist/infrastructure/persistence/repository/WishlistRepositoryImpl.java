package com.marketplace.wishlist.infrastructure.persistence.repository;

import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.repository.WishlistRepository;
import com.marketplace.wishlist.infrastructure.persistence.entity.WishlistJpaEntity;
import com.marketplace.wishlist.infrastructure.persistence.mapper.WishlistPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WishlistRepositoryImpl implements WishlistRepository {

    private final WishlistJpaRepository jpaRepository;

    @Override
    public Wishlist save(Wishlist wishlist) {
        WishlistJpaEntity jpa = WishlistPersistenceMapper.toJpaEntity(wishlist);
        WishlistJpaEntity saved = jpaRepository.save(jpa);
        return WishlistPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Wishlist> findById(Long id) {
        return jpaRepository.findById(id)
            .map(WishlistPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Wishlist> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId)
            .map(WishlistPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Wishlist> findByUserIdAndIsDefault(Long userId, boolean isDefault) {
        return jpaRepository.findByUserIdAndIsDefault(userId, isDefault)
            .map(WishlistPersistenceMapper::toDomain);
    }

    @Override
    public List<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(WishlistPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByUserIdAndName(Long userId, String name) {
        return jpaRepository.existsByUserIdAndName(userId, name);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }

    @Override
    public void delete(Wishlist wishlist) {
        jpaRepository.deleteById(wishlist.getId());
    }
}