package com.marketplace.wishlist.application.service;

import com.marketplace.wishlist.application.dto.*;

public interface WishlistService {

    WishlistItemResponse addToWishlist(Long userId, AddToWishlistRequest request);

    void removeFromWishlist(Long userId, Long productId, Long variantId);

    void removeWishlistItemById(Long userId, Long itemId);

    WishlistResponse getWishlist(Long userId);

    WishlistSummaryResponse getWishlistSummary(Long userId);

    void clearWishlist(Long userId);
}