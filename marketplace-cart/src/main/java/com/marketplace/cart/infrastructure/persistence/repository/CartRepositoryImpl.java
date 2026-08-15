package com.marketplace.cart.infrastructure.persistence.repository;

import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.repository.CartRepository;
import com.marketplace.cart.domain.valueobject.CartStatus;
import com.marketplace.cart.infrastructure.persistence.entity.CartJpaEntity;
import com.marketplace.cart.infrastructure.persistence.mapper.CartPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository jpaRepository;

    @Override
    public Cart save(Cart cart) {
        CartJpaEntity jpa = CartPersistenceMapper.toJpaEntity(cart);
        CartJpaEntity saved = jpaRepository.save(jpa);
        return CartPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return jpaRepository.findById(id)
            .map(CartPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId)
            .map(CartPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Cart> findByUserIdAndStatus(Long userId, CartStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status)
            .map(CartPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Cart> findBySessionId(String sessionId) {
        return jpaRepository.findBySessionId(sessionId)
            .map(CartPersistenceMapper::toDomain);
    }

    @Override
    public List<Cart> findByUserIdAndStatusIn(Long userId, List<CartStatus> statuses) {
        return jpaRepository.findByUserIdAndStatusIn(userId, statuses).stream()
            .map(CartPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public long countByUserIdAndStatus(Long userId, CartStatus status) {
        return jpaRepository.countByUserIdAndStatus(userId, status);
    }

    @Override
    public void delete(Cart cart) {
        jpaRepository.deleteById(cart.getId());
    }

    @Override
    public void deleteByStatus(CartStatus status) {
        jpaRepository.deleteByStatus(status);
    }
}