package com.marketplace.product.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryMovement {

    public enum MovementType {
        IN,
        OUT,
        ADJUSTMENT,
        RESERVED,
        RELEASED
    }

    public enum ReferenceType {
        ORDER,
        RETURN,
        MANUAL,
        RESTOCK,
        ADJUSTMENT
    }

    private Long id;
    private ProductVariant variant;
    private MovementType type;
    private Integer quantity;
    private ReferenceType referenceType;
    private Long referenceId;
    private String notes;
    private Long createdBy;
    private Instant createdAt;

    public InventoryMovement(ProductVariant variant, MovementType type, int quantity) {
        this.variant = variant;
        this.type = type;
        this.quantity = quantity;
    }

    public static InventoryMovement stockIn(ProductVariant variant, int quantity, String notes) {
        InventoryMovement movement = new InventoryMovement(variant, MovementType.IN, quantity);
        movement.setNotes(notes);
        return movement;
    }

    public static InventoryMovement stockOut(ProductVariant variant, int quantity, ReferenceType referenceType, Long referenceId) {
        InventoryMovement movement = new InventoryMovement(variant, MovementType.OUT, quantity);
        movement.setReferenceType(referenceType);
        movement.setReferenceId(referenceId);
        return movement;
    }

    public static InventoryMovement adjustment(ProductVariant variant, int quantity, String notes) {
        InventoryMovement movement = new InventoryMovement(variant, MovementType.ADJUSTMENT, quantity);
        movement.setNotes(notes);
        return movement;
    }

    public static InventoryMovement reserve(ProductVariant variant, int quantity, Long orderId) {
        InventoryMovement movement = new InventoryMovement(variant, MovementType.RESERVED, quantity);
        movement.setReferenceType(ReferenceType.ORDER);
        movement.setReferenceId(orderId);
        return movement;
    }

    public static InventoryMovement release(ProductVariant variant, int quantity, Long orderId) {
        InventoryMovement movement = new InventoryMovement(variant, MovementType.RELEASED, quantity);
        movement.setReferenceType(ReferenceType.ORDER);
        movement.setReferenceId(orderId);
        return movement;
    }
}
