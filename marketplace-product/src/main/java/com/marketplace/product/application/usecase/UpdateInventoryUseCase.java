package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.UpdateInventoryRequest;
import com.marketplace.product.application.dto.VariantResponse;
import com.marketplace.product.domain.entity.InventoryMovement;
import com.marketplace.product.domain.entity.ProductVariant;
import com.marketplace.product.domain.event.InventoryUpdatedEvent;
import com.marketplace.product.domain.exception.VariantNotFoundException;
import com.marketplace.product.domain.repository.InventoryMovementRepository;
import com.marketplace.product.domain.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateInventoryUseCase {

    private final ProductVariantRepository variantRepository;
    private final InventoryMovementRepository movementRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VariantResponse execute(Long variantId, UpdateInventoryRequest request) {
        ProductVariant variant = variantRepository.findById(variantId)
            .orElseThrow(() -> new VariantNotFoundException(variantId));

        int previousQuantity = variant.getInventoryQuantity();

        variant.updateInventory(request.getQuantity());
        ProductVariant savedVariant = variantRepository.save(variant);

        InventoryMovement.MovementType movementType = InventoryMovement.MovementType.ADJUSTMENT;
        InventoryMovement.ReferenceType referenceType = InventoryMovement.ReferenceType.MANUAL;

        if (request.getReferenceType() != null) {
            try {
                referenceType = InventoryMovement.ReferenceType.valueOf(request.getReferenceType().toUpperCase());
                if (referenceType == InventoryMovement.ReferenceType.ORDER) {
                    movementType = InventoryMovement.MovementType.OUT;
                } else if (referenceType == InventoryMovement.ReferenceType.RETURN) {
                    movementType = InventoryMovement.MovementType.IN;
                }
            } catch (IllegalArgumentException e) {
                referenceType = InventoryMovement.ReferenceType.MANUAL;
            }
        }

        InventoryMovement movement = new InventoryMovement(savedVariant, movementType, request.getQuantity());
        movement.setReferenceType(referenceType);
        movement.setReferenceId(request.getReferenceId());
        movement.setNotes(request.getNotes());
        movementRepository.save(movement);

        eventPublisher.publishEvent(new InventoryUpdatedEvent(
            savedVariant.getId(),
            savedVariant.getProduct().getId(),
            savedVariant.getName(),
            previousQuantity,
            savedVariant.getInventoryQuantity(),
            movementType.name()
        ));

        return VariantResponse.from(savedVariant);
    }
}