package com.marketplace.cart.application.service;

import com.marketplace.cart.application.dto.*;
import com.marketplace.cart.application.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final AddToCartUseCase addToCartUseCase;
    private final RemoveFromCartUseCase removeFromCartUseCase;
    private final RemoveCartItemByIdUseCase removeCartItemByIdUseCase;
    private final UpdateCartItemUseCase updateCartItemUseCase;
    private final GetCartUseCase getCartUseCase;
    private final ClearCartUseCase clearCartUseCase;

    @Override
    @Transactional
    public CartItemResponse addToCart(Long userId, AddToCartRequest request) {
        return addToCartUseCase.execute(userId, request);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId, Long variantId) {
        removeFromCartUseCase.execute(userId, productId, variantId);
    }

    @Override
    @Transactional
    public void removeCartItemById(Long userId, Long itemId) {
        removeCartItemByIdUseCase.execute(userId, itemId);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(Long userId, Long productId, Long variantId, UpdateCartItemRequest request) {
        return updateCartItemUseCase.execute(userId, productId, variantId, request);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        return getCartUseCase.execute(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public CartSummaryResponse getCartSummary(Long userId) {
        return getCartUseCase.executeIfExists(userId)
            .map(CartSummaryResponse::from)
            .orElse(CartSummaryResponse.builder()
                .itemCount(0)
                .subtotal(java.math.BigDecimal.ZERO)
                .total(java.math.BigDecimal.ZERO)
                .currency("USD")
                .build());
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        clearCartUseCase.execute(userId);
    }
}