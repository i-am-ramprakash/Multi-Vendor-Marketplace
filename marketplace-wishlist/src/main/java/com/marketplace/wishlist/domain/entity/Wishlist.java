package com.marketplace.wishlist.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {

    private static final int MAX_ITEMS = 100;

    private Long id;
    private Long userId;
    private String name;
    private boolean isDefault;
    private Instant createdAt;
    private Instant updatedAt;

    private final List<WishlistItem> items = new ArrayList<>();

    public Wishlist(Long userId) {
        this.userId = userId;
        this.name = "My Wishlist";
        this.isDefault = true;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Wishlist(Long userId, String name) {
        this.userId = userId;
        this.name = name;
        this.isDefault = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public WishlistItem addItem(Long productId, Long variantId, String productName,
                               String variantName, BigDecimal unitPrice, String imageUrl,
                               String vendorName, Long vendorId) {
        if (items.size() >= MAX_ITEMS) {
            throw new IllegalStateException("Wishlist cannot have more than " + MAX_ITEMS + " items");
        }

        if (hasProduct(productId, variantId)) {
            throw new IllegalStateException("Product already exists in wishlist");
        }

        WishlistItem item = new WishlistItem(productId, variantId, productName, variantName,
                                            unitPrice, imageUrl, vendorName, vendorId);
        items.add(item);
        this.updatedAt = Instant.now();
        return item;
    }

    public void removeItem(Long productId, Long variantId) {
        WishlistItem item = findItem(productId, variantId)
            .orElseThrow(() -> new IllegalStateException("Item not found in wishlist"));

        items.remove(item);
        this.updatedAt = Instant.now();
    }

    public void removeItemById(Long itemId) {
        WishlistItem item = items.stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Item not found in wishlist"));

        items.remove(item);
        this.updatedAt = Instant.now();
    }

    public void clear() {
        items.clear();
        this.updatedAt = Instant.now();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.size();
    }

    public boolean hasProduct(Long productId, Long variantId) {
        return findItem(productId, variantId).isPresent();
    }

    public Optional<WishlistItem> findItem(Long productId, Long variantId) {
        return items.stream()
            .filter(item -> item.getProductId().equals(productId))
            .filter(item -> {
                if (variantId == null) {
                    return item.getVariantId() == null;
                }
                return variantId.equals(item.getVariantId());
            })
            .findFirst();
    }

    public Optional<WishlistItem> findItemById(Long itemId) {
        return items.stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst();
    }
}