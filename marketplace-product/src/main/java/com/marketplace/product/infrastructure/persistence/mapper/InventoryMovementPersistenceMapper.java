package com.marketplace.product.infrastructure.persistence.mapper;

import com.marketplace.product.domain.entity.InventoryMovement;
import com.marketplace.product.infrastructure.persistence.entity.InventoryMovementJpaEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InventoryMovementPersistenceMapper {

    private InventoryMovementPersistenceMapper() {}

    public static InventoryMovementJpaEntity toJpaEntity(InventoryMovement domain) {
        if (domain == null) return null;

        InventoryMovementJpaEntity jpa = new InventoryMovementJpaEntity();
        jpa.setId(domain.getId());
        jpa.setType(domain.getType());
        jpa.setQuantity(domain.getQuantity());
        jpa.setReferenceType(domain.getReferenceType());
        jpa.setReferenceId(domain.getReferenceId());
        jpa.setNotes(domain.getNotes());
        jpa.setCreatedBy(domain.getCreatedBy());
        jpa.setCreatedAt(domain.getCreatedAt());

        if (domain.getVariant() != null) {
            com.marketplace.product.infrastructure.persistence.entity.ProductVariantJpaEntity variantJpa = new com.marketplace.product.infrastructure.persistence.entity.ProductVariantJpaEntity();
            variantJpa.setId(domain.getVariant().getId());
            jpa.setVariant(variantJpa);
        }

        return jpa;
    }

    public static InventoryMovement toDomain(InventoryMovementJpaEntity jpa) {
        if (jpa == null) return null;

        InventoryMovement movement = new InventoryMovement(
            null,
            jpa.getType(),
            jpa.getQuantity()
        );

        setId(movement, jpa.getId());
        movement.setReferenceType(jpa.getReferenceType());
        movement.setReferenceId(jpa.getReferenceId());
        movement.setNotes(jpa.getNotes());
        movement.setCreatedBy(jpa.getCreatedBy());
        movement.setCreatedAt(jpa.getCreatedAt());

        return movement;
    }

    public static List<InventoryMovement> toDomainList(List<InventoryMovementJpaEntity> jpaList) {
        if (jpaList == null) return Collections.emptyList();
        List<InventoryMovement> domains = new ArrayList<>();
        for (InventoryMovementJpaEntity jpa : jpaList) {
            domains.add(toDomain(jpa));
        }
        return domains;
    }

    private static void setId(InventoryMovement movement, Long id) {
        try {
            Field field = InventoryMovement.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(movement, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set inventory movement ID", e);
        }
    }
}