package com.marketplace.cart.application.usecase;

import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.entity.CartItem;
import com.marketplace.cart.domain.event.ItemRemovedFromCartEvent;
import com.marketplace.cart.domain.exception.CartNotFoundException;
import com.marketplace.cart.domain.repository.CartRepository;
import com.marketplace.cart.domain.valueobject.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveCartItemByIdUseCase {

    private final CartRepository cartRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(Long userId, Long itemId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .orElseThrow(() -> new CartNotFoundException("No active cart found for user: " + userId));

        CartItem item = cart.findItemById(itemId)
            .orElseThrow(() -> new com.marketplace.cart.domain.exception.CartItemNotFoundException(itemId));

        Long productId = item.getProductId();
        Long variantId = item.getVariantId();
        Long cartId = cart.getId();

        cart.removeItemById(itemId);
        cartRepository.save(cart);

        eventPublisher.publishEvent(new ItemRemovedFromCartEvent(
            this,
            cartId,
            userId,
            productId,
            variantId
        ));
    }
}