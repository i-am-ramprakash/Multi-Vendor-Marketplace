package com.marketplace.cart.application.service;

import com.marketplace.cart.application.dto.*;

public interface CartService {

    CartItemResponse addToCart(Long userId, AddToCartRequest request);

    void removeFromCart(Long userId, Long productId, Long variantId);

    void removeCartItemById(Long userId, Long itemId);

    CartItemResponse updateCartItem(Long userId, Long productId, Long variantId, UpdateCartItemRequest request);

    CartResponse getCart(Long userId);

    CartSummaryResponse getCartSummary(Long userId);

    void clearCart(Long userId);
}