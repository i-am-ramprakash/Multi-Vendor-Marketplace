package com.marketplace.wishlist.application.usecase;

import com.marketplace.wishlist.application.dto.AddToWishlistRequest;
import com.marketplace.wishlist.application.dto.WishlistItemResponse;
import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.entity.WishlistItem;
import com.marketplace.wishlist.domain.event.ItemAddedToWishlistEvent;
import com.marketplace.wishlist.domain.exception.DuplicateWishlistItemException;
import com.marketplace.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class AddToWishlistUseCase {

    private final WishlistRepository wishlistRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public WishlistItemResponse execute(Long userId, AddToWishlistRequest request) {
        // Get or create default wishlist
        Wishlist wishlist = getOrCreateDefaultWishlist(userId);

        // Check if product already exists
        if (wishlist.hasProduct(request.getProductId(), request.getVariantId())) {
            throw new DuplicateWishlistItemException(request.getProductId(), request.getVariantId());
        }

        // Add item
        WishlistItem item = wishlist.addItem(
            request.getProductId(),
            request.getVariantId(),
            request.getProductName(),
            request.getVariantName(),
            request.getUnitPrice(),
            request.getImageUrl(),
            request.getVendorName(),
            request.getVendorId()
        );

        wishlist = wishlistRepository.save(wishlist);

        // Publish event
        eventPublisher.publishEvent(new ItemAddedToWishlistEvent(
            this,
            wishlist.getId(),
            userId,
            request.getProductId(),
            request.getVariantId()
        ));

        return WishlistItemResponse.from(item);
    }

    private Wishlist getOrCreateDefaultWishlist(Long userId) {
        return wishlistRepository.findByUserIdAndIsDefault(userId, true)
            .orElseGet(() -> {
                Wishlist newWishlist = new Wishlist(userId);
                return wishlistRepository.save(newWishlist);
            });
    }
}