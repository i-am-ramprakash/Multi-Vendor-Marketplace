package com.marketplace.cart.application.usecase;

import com.marketplace.cart.application.dto.AddToCartRequest;
import com.marketplace.cart.application.dto.CartItemResponse;
import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.entity.CartItem;
import com.marketplace.cart.domain.event.ItemAddedToCartEvent;
import com.marketplace.cart.domain.exception.InsufficientInventoryException;
import com.marketplace.cart.domain.repository.CartRepository;
import com.marketplace.cart.domain.service.InventoryValidationService;
import com.marketplace.cart.domain.valueobject.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AddToCartUseCase {

    private final CartRepository cartRepository;
    private final InventoryValidationService inventoryValidationService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CartItemResponse execute(Long userId, AddToCartRequest request) {
        // Validate inventory availability
        int requestedQuantity = request.getQuantity();
        if (!inventoryValidationService.isProductAvailable(request.getProductId(), request.getVariantId(), requestedQuantity)) {
            int available = inventoryValidationService.getAvailableQuantity(request.getProductId(), request.getVariantId());
            throw new InsufficientInventoryException(request.getProductName(), requestedQuantity, available);
        }

        // Get or create cart
        Cart cart = getOrCreateCart(userId);

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.findItem(request.getProductId(), request.getVariantId());

        CartItem item;
        if (existingItem.isPresent()) {
            // Update quantity
            item = existingItem.get();
            int newQuantity = item.getQuantity() + requestedQuantity;

            // Validate new quantity against inventory
            if (!inventoryValidationService.isProductAvailable(request.getProductId(), request.getVariantId(), newQuantity)) {
                int available = inventoryValidationService.getAvailableQuantity(request.getProductId(), request.getVariantId());
                throw new InsufficientInventoryException(request.getProductName(), newQuantity, available);
            }

            item.updateQuantity(newQuantity);
        } else {
            // Add new item
            int maxQuantity = inventoryValidationService.getMaxAllowedQuantity(request.getProductId(), request.getVariantId());

            item = cart.addItem(
                request.getProductId(),
                request.getVariantId(),
                request.getProductName(),
                request.getVariantName(),
                request.getUnitPrice(),
                requestedQuantity,
                request.getImageUrl()
            );

            item.setMaxQuantity(maxQuantity);
        }

        cart = cartRepository.save(cart);

        // Publish event
        eventPublisher.publishEvent(new ItemAddedToCartEvent(
            this,
            cart.getId(),
            userId,
            request.getProductId(),
            request.getVariantId(),
            requestedQuantity
        ));

        return CartItemResponse.from(item);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
            .orElseGet(() -> {
                Cart newCart = new Cart(userId);
                return cartRepository.save(newCart);
            });
    }
}