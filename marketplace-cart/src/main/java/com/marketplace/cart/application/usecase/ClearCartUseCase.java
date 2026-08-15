package com.marketplace.cart.application.usecase;

import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.event.CartClearedEvent;
import com.marketplace.cart.domain.exception.CartNotFoundException;
import com.marketplace.cart.domain.repository.CartRepository;
import com.marketplace.cart.domain.valueobject.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ClearCartUseCase {

    private final CartRepository cartRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .orElseThrow(() -> new CartNotFoundException("No active cart found for user: " + userId));

        Long cartId = cart.getId();
        cart.clear();
        cartRepository.save(cart);

        eventPublisher.publishEvent(new CartClearedEvent(this, cartId, userId));
    }
}