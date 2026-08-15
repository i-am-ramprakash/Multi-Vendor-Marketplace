package com.marketplace.cart.domain.entity;

import com.marketplace.cart.domain.valueobject.CartStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    private static final int MAX_ITEMS = 50;

    private Long id;
    private Long userId;
    private String sessionId;
    private CartStatus status;
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal total;
    private String couponCode;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant expiresAt;

    private final List<CartItem> items = new ArrayList<>();

    public Cart(Long userId) {
        this.userId = userId;
        this.sessionId = UUID.randomUUID().toString();
        this.status = CartStatus.ACTIVE;
        this.currency = "USD";
        this.subtotal = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.expiresAt = Instant.now().plusSeconds(7 * 24 * 60 * 60); // 7 days
    }

    public Cart(String sessionId) {
        this.sessionId = sessionId;
        this.status = CartStatus.ACTIVE;
        this.currency = "USD";
        this.subtotal = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.expiresAt = Instant.now().plusSeconds(24 * 60 * 60); // 24 hours for anonymous
    }

    public CartItem addItem(Long productId, Long variantId, String productName, String variantName,
                           BigDecimal unitPrice, int quantity, String imageUrl) {
        if (!status.canBeModified()) {
            throw new IllegalStateException("Cannot modify cart in " + status + " status");
        }

        if (items.size() >= MAX_ITEMS) {
            throw new IllegalStateException("Cart cannot have more than " + MAX_ITEMS + " items");
        }

        Optional<CartItem> existingItem = findItem(productId, variantId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.updateQuantity(item.getQuantity() + quantity);
            recalculateTotals();
            this.updatedAt = Instant.now();
            return item;
        }

        CartItem newItem = new CartItem(productId, variantId, productName, variantName,
                                       unitPrice, quantity, imageUrl);
        items.add(newItem);
        recalculateTotals();
        this.updatedAt = Instant.now();
        return newItem;
    }

    public void removeItem(Long productId, Long variantId) {
        if (!status.canBeModified()) {
            throw new IllegalStateException("Cannot modify cart in " + status + " status");
        }

        CartItem item = findItem(productId, variantId)
            .orElseThrow(() -> new IllegalStateException("Item not found in cart"));

        items.remove(item);
        recalculateTotals();
        this.updatedAt = Instant.now();
    }

    public void removeItemById(Long itemId) {
        if (!status.canBeModified()) {
            throw new IllegalStateException("Cannot modify cart in " + status + " status");
        }

        CartItem item = items.stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Item not found in cart"));

        items.remove(item);
        recalculateTotals();
        this.updatedAt = Instant.now();
    }

    public void updateItemQuantity(Long productId, Long variantId, int quantity) {
        if (!status.canBeModified()) {
            throw new IllegalStateException("Cannot modify cart in " + status + " status");
        }

        CartItem item = findItem(productId, variantId)
            .orElseThrow(() -> new IllegalStateException("Item not found in cart"));

        if (quantity <= 0) {
            items.remove(item);
        } else {
            item.updateQuantity(quantity);
        }

        recalculateTotals();
        this.updatedAt = Instant.now();
    }

    public void clear() {
        if (!status.canBeModified()) {
            throw new IllegalStateException("Cannot modify cart in " + status + " status");
        }

        items.clear();
        recalculateTotals();
        this.updatedAt = Instant.now();
    }

    public void applyCoupon(String couponCode, BigDecimal discountAmount) {
        this.couponCode = couponCode;
        this.discountAmount = discountAmount;
        recalculateTotals();
        this.updatedAt = Instant.now();
    }

    public void removeCoupon() {
        this.couponCode = null;
        this.discountAmount = BigDecimal.ZERO;
        recalculateTotals();
        this.updatedAt = Instant.now();
    }

    public void convertToOrder() {
        this.status = CartStatus.CONVERTED;
        this.updatedAt = Instant.now();
    }

    public void markAsAbandoned() {
        this.status = CartStatus.ABANDONED;
        this.updatedAt = Instant.now();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }

    public boolean hasItem(Long productId, Long variantId) {
        return findItem(productId, variantId).isPresent();
    }

    public Optional<CartItem> findItem(Long productId, Long variantId) {
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

    public Optional<CartItem> findItemById(Long itemId) {
        return items.stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst();
    }

    private void recalculateTotals() {
        this.subtotal = items.stream()
            .map(CartItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.total = this.subtotal
            .add(this.taxAmount)
            .subtract(this.discountAmount);
    }
}