package com.marketplace.order.domain.entity;

import com.marketplace.order.domain.valueobject.Money;
import com.marketplace.order.domain.valueobject.OrderNumber;
import com.marketplace.order.domain.valueobject.OrderStatus;
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
public class Order {

    private Long id;
    private OrderNumber orderNumber;
    private Long userId;
    private OrderStatus status;
    private Money subtotal;
    private Money taxAmount;
    private Money shippingAmount;
    private Money discountAmount;
    private Money commissionAmount;
    private Money total;
    private String currency;
    private String paymentMethod;
    private String paymentReference;
    private String shippingAddress;
    private String billingAddress;
    private String notes;
    private String cancellationReason;
    private Instant shippedAt;
    private Instant deliveredAt;
    private Instant cancelledAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    private final List<OrderItem> items = new ArrayList<>();
    private final List<OrderStatusHistory> statusHistory = new ArrayList<>();
    private final List<OrderAuditLog> auditLogs = new ArrayList<>();

    public Order(Long userId, String currency) {
        this.orderNumber = OrderNumber.generate();
        this.userId = userId;
        this.status = OrderStatus.PENDING;
        this.currency = currency;
        this.subtotal = Money.zero(currency);
        this.taxAmount = Money.zero(currency);
        this.shippingAmount = Money.zero(currency);
        this.discountAmount = Money.zero(currency);
        this.commissionAmount = Money.zero(currency);
        this.total = Money.zero(currency);
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.version = 0L;

        addStatusHistory("Order created");
    }

    public OrderItem addItem(Long productId, Long variantId, Long vendorId, String productName,
                            String variantName, BigDecimal unitPrice, int quantity, BigDecimal commissionRate) {
        OrderItem item = new OrderItem(productId, variantId, vendorId, productName, variantName,
                                       unitPrice, quantity, commissionRate, currency);
        items.add(item);
        recalculateTotals();
        this.updatedAt = Instant.now();
        return item;
    }

    public void removeItem(Long itemId) {
        OrderItem item = items.stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Order item not found: " + itemId));

        items.remove(item);
        recalculateTotals();
        this.updatedAt = Instant.now();
    }

    public void confirm() {
        if (!status.canTransitionTo(OrderStatus.CONFIRMED)) {
            throw new IllegalStateException("Cannot confirm order in " + status + " status");
        }
        this.status = OrderStatus.CONFIRMED;
        addStatusHistory("Order confirmed");
        this.updatedAt = Instant.now();
    }

    public void startProcessing() {
        if (!status.canTransitionTo(OrderStatus.PROCESSING)) {
            throw new IllegalStateException("Cannot process order in " + status + " status");
        }
        this.status = OrderStatus.PROCESSING;
        addStatusHistory("Order processing started");
        this.updatedAt = Instant.now();
    }

    public void ship(String trackingNumber) {
        if (!status.canTransitionTo(OrderStatus.SHIPPED)) {
            throw new IllegalStateException("Cannot ship order in " + status + " status");
        }
        this.status = OrderStatus.SHIPPED;
        this.shippedAt = Instant.now();
        addStatusHistory("Order shipped. Tracking: " + trackingNumber);
        this.updatedAt = Instant.now();
    }

    public void deliver() {
        if (!status.canTransitionTo(OrderStatus.DELIVERED)) {
            throw new IllegalStateException("Cannot deliver order in " + status + " status");
        }
        this.status = OrderStatus.DELIVERED;
        this.deliveredAt = Instant.now();
        addStatusHistory("Order delivered");
        this.updatedAt = Instant.now();
    }

    public void cancel(String reason) {
        if (!status.canTransitionTo(OrderStatus.CANCELLED)) {
            throw new IllegalStateException("Cannot cancel order in " + status + " status");
        }
        this.status = OrderStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancelledAt = Instant.now();
        addStatusHistory("Order cancelled. Reason: " + reason);
        this.updatedAt = Instant.now();
    }

    public void refund(String reason) {
        if (!status.canTransitionTo(OrderStatus.REFUNDED)) {
            throw new IllegalStateException("Cannot refund order in " + status + " status");
        }
        this.status = OrderStatus.REFUNDED;
        this.cancellationReason = reason;
        addStatusHistory("Order refunded. Reason: " + reason);
        this.updatedAt = Instant.now();
    }

    public void updateTracking(String trackingNumber) {
        this.shippedAt = Instant.now();
        addStatusHistory("Tracking updated: " + trackingNumber);
        this.updatedAt = Instant.now();
    }

    public void setPaymentInfo(String paymentMethod, String paymentReference) {
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
        this.updatedAt = Instant.now();
    }

    public void setShippingAddress(String address) {
        this.shippingAddress = address;
        this.updatedAt = Instant.now();
    }

    public void setBillingAddress(String address) {
        this.billingAddress = address;
        this.updatedAt = Instant.now();
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.updatedAt = Instant.now();
    }

    public void addAuditLog(String action, Long performedBy, String details) {
        OrderAuditLog auditLog = new OrderAuditLog(action, performedBy, details);
        auditLogs.add(auditLog);
        this.updatedAt = Instant.now();
    }

    private void addStatusHistory(String notes) {
        OrderStatusHistory history = new OrderStatusHistory(this.status, notes);
        statusHistory.add(history);
    }

    private void recalculateTotals() {
        this.subtotal = items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(Money.zero(currency), Money::add);

        this.commissionAmount = items.stream()
            .map(OrderItem::getCommissionAmount)
            .reduce(Money.zero(currency), Money::add);

        this.total = this.subtotal
            .add(this.taxAmount)
            .add(this.shippingAmount)
            .subtract(this.discountAmount);
    }

    public boolean isActive() {
        return status.isActive();
    }

    public boolean canBeCancelled() {
        return status.canBeCancelled();
    }

    public boolean canBeRefunded() {
        return status.canBeRefunded();
    }

    public Optional<OrderItem> findItemById(Long itemId) {
        return items.stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst();
    }

    public List<OrderItem> getItemsByVendorId(Long vendorId) {
        return items.stream()
            .filter(i -> i.getVendorId().equals(vendorId))
            .toList();
    }

    public Money getVendorTotal(Long vendorId) {
        return items.stream()
            .filter(i -> i.getVendorId().equals(vendorId))
            .map(OrderItem::getSubtotal)
            .reduce(Money.zero(currency), Money::add);
    }

    public Money getVendorCommission(Long vendorId) {
        return items.stream()
            .filter(i -> i.getVendorId().equals(vendorId))
            .map(OrderItem::getCommissionAmount)
            .reduce(Money.zero(currency), Money::add);
    }

    public int getTotalItemCount() {
        return items.stream()
            .mapToInt(OrderItem::getQuantity)
            .sum();
    }
}