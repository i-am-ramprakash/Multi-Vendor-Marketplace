package com.marketplace.cart.application.usecase;

import com.marketplace.cart.application.dto.CartResponse;
import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.exception.CartNotFoundException;
import com.marketplace.cart.domain.repository.CartRepository;
import com.marketplace.cart.domain.service.InventoryValidationService;
import com.marketplace.cart.domain.valueobject.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetCartUseCase {

    private final CartRepository cartRepository;
    private final InventoryValidationService inventoryValidationService;

    @Transactional(readOnly = true)
    public CartResponse execute(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .orElseThrow(() -> new CartNotFoundException("No active cart found for user: " + userId));

        // Validate inventory for all items
        cart.getItems().forEach(item -> {
            boolean available = inventoryValidationService.isProductAvailable(
                item.getProductId(),
                item.getVariantId(),
                item.getQuantity()
            );
            item.setInventoryAvailable(available);

            int maxQuantity = inventoryValidationService.getMaxAllowedQuantity(
                item.getProductId(),
                item.getVariantId()
            );
            item.setMaxQuantity(maxQuantity);
        });

        return CartResponse.from(cart);
    }

    @Transactional(readOnly = true)
    public Optional<CartResponse> executeIfExists(Long userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .map(CartResponse::from);
    }
}