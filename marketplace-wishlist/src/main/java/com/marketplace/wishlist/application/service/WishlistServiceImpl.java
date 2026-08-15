package com.marketplace.wishlist.application.service;

import com.marketplace.wishlist.application.dto.*;
import com.marketplace.wishlist.application.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final AddToWishlistUseCase addToWishlistUseCase;
    private final RemoveFromWishlistUseCase removeFromWishlistUseCase;
    private final RemoveWishlistItemByIdUseCase removeWishlistItemByIdUseCase;
    private final GetWishlistUseCase getWishlistUseCase;
    private final ClearWishlistUseCase clearWishlistUseCase;

    @Override
    @Transactional
    public WishlistItemResponse addToWishlist(Long userId, AddToWishlistRequest request) {
        return addToWishlistUseCase.execute(userId, request);
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long userId, Long productId, Long variantId) {
        removeFromWishlistUseCase.execute(userId, productId, variantId);
    }

    @Override
    @Transactional
    public void removeWishlistItemById(Long userId, Long itemId) {
        removeWishlistItemByIdUseCase.execute(userId, itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public WishlistResponse getWishlist(Long userId) {
        return getWishlistUseCase.execute(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public WishlistSummaryResponse getWishlistSummary(Long userId) {
        return getWishlistUseCase.executeIfExists(userId)
            .map(WishlistSummaryResponse::from)
            .orElse(WishlistSummaryResponse.builder()
                .itemCount(0)
                .build());
    }

    @Override
    @Transactional
    public void clearWishlist(Long userId) {
        clearWishlistUseCase.execute(userId);
    }
}