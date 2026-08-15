package com.marketplace.wishlist.application.usecase;

import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.entity.WishlistItem;
import com.marketplace.wishlist.domain.event.ItemRemovedFromWishlistEvent;
import com.marketplace.wishlist.domain.exception.WishlistItemNotFoundException;
import com.marketplace.wishlist.domain.exception.WishlistNotFoundException;
import com.marketplace.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveWishlistItemByIdUseCase {

    private final WishlistRepository wishlistRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(Long userId, Long itemId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndIsDefault(userId, true)
            .orElseThrow(() -> new WishlistNotFoundException("No default wishlist found for user: " + userId));

        WishlistItem item = wishlist.findItemById(itemId)
            .orElseThrow(() -> new WishlistItemNotFoundException(itemId));

        Long productId = item.getProductId();
        Long variantId = item.getVariantId();
        Long wishlistId = wishlist.getId();

        wishlist.removeItemById(itemId);
        wishlistRepository.save(wishlist);

        eventPublisher.publishEvent(new ItemRemovedFromWishlistEvent(
            this,
            wishlistId,
            userId,
            productId,
            variantId
        ));
    }
}