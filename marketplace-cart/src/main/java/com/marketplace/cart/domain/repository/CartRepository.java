package com.marketplace.cart.domain.repository;

import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.valueobject.CartStatus;

import java.util.List;
import java.util.Optional;

public interface CartRepository {

    Cart save(Cart cart);

    Optional<Cart> findById(Long id);

    Optional<Cart> findByUserId(Long userId);

    Optional<Cart> findByUserIdAndStatus(Long userId, CartStatus status);

    Optional<Cart> findBySessionId(String sessionId);

    List<Cart> findByUserIdAndStatusIn(Long userId, List<CartStatus> statuses);

    long countByUserIdAndStatus(Long userId, CartStatus status);

    void delete(Cart cart);

    void deleteByStatus(CartStatus status);
}