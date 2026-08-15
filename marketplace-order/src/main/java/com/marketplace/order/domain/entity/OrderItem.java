package com.marketplace.order.domain.entity;

import com.marketplace.order.domain.valueobject.Money;
import com.marketplace.order.domain.valueobject.OrderItemStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    private Long id;
    private Long productId;
    private Long variantId;
    private Long vendorId;
    private String productName;
    private String variantName;
    private Money unitPrice;
    private int quantity;
    private Money subtotal;
    private Money taxAmount;
    private Money commissionRate;
    private Money commissionAmount;
    private Money vendorPayout;
    private OrderItemStatus status;
    private String sku;
    private String imageUrl;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;

    public OrderItem(Long productId, Long variantId, Long vendorId, String productName,
                    String variantName, BigDecimal unitPrice, int quantity,
                    BigDecimal commissionRate, String currency) {
        this.productId = productId;
        this.variantId = variantId;
        this.vendorId = vendorId;
        this.productName = productName;
        this.variantName = variantName;
        this.unitPrice = Money.of(unitPrice, currency);
        this.quantity = quantity;
        this.commissionRate = Money.of(commissionRate, currency);
        this.status = OrderItemStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        calculateAmounts();
    }

    public void calculateAmounts() {
        this.subtotal = this.unitPrice.multiply(this.quantity);
        this.commissionAmount = this.subtotal.percentage(this.commissionRate.getAmount());
        this.vendorPayout = this.subtotal.subtract(this.commissionAmount);
        this.taxAmount = Money.zero(this.unitPrice.getCurrency());
        this.updatedAt = Instant.now();
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
        calculateAmounts();
        this.updatedAt = Instant.now();
    }

    public void updateStatus(OrderItemStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Cannot transition from " + this.status + " to " + newStatus);
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    public void cancel(String reason) {
        updateStatus(OrderItemStatus.CANCELLED);
        this.notes = reason;
    }

    public void refund(String reason) {
        updateStatus(OrderItemStatus.REFUNDED);
        this.notes = reason;
    }

    public boolean isActive() {
        return status.isActive();
    }

    public boolean hasVariant() {
        return variantId != null;
    }
}