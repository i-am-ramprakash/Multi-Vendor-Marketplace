package com.marketplace.cart.application.usecase;

import com.marketplace.cart.application.dto.CartItemResponse;
import com.marketplace.cart.application.dto.UpdateCartItemRequest;
import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.entity.CartItem;
import com.marketplace.cart.domain.exception.CartNotFoundException;
import com.marketplace.cart.domain.exception.InsufficientInventoryException;
import com.marketplace.cart.domain.repository.CartRepository;
import com.marketplace.cart.domain.service.InventoryValidationService;
import com.marketplace.cart.domain.valueobject.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateCartItemUseCase {

    private final CartRepository cartRepository;
    private final InventoryValidationService inventoryValidationService;

    @Transactional
    public CartItemResponse execute(Long userId, Long productId, Long variantId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .orElseThrow(() -> new CartNotFoundException("No active cart found for user: " + userId));

        CartItem item = cart.findItem(productId, variantId)
            .orElseThrow(() -> new com.marketplace.cart.domain.exception.CartItemNotFoundException(productId, variantId));

        int newQuantity = request.getQuantity();

        if (newQuantity > 0) {
            // Validate inventory availability
            if (!inventoryValidationService.isProductAvailable(productId, variantId, newQuantity)) {
                int available = inventoryValidationService.getAvailableQuantity(productId, variantId);
                throw new InsufficientInventoryException(item.getProductName(), newQuantity, available);
            }

            item.updateQuantity(newQuantity);
        } else {
            cart.removeItem(productId, variantId);
        }

        cart = cartRepository.save(cart);

        return CartItemResponse.from(item);
    }
}