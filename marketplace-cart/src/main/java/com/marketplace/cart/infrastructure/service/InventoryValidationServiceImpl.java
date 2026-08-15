package com.marketplace.cart.infrastructure.service;

import com.marketplace.cart.domain.service.InventoryValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryValidationServiceImpl implements InventoryValidationService {

    private static final int DEFAULT_MAX_QUANTITY = 10;

    @Override
    public boolean isProductAvailable(Long productId, Long variantId, int quantity) {
        int available = getAvailableQuantity(productId, variantId);
        return available >= quantity;
    }

    @Override
    public int getAvailableQuantity(Long productId, Long variantId) {
        // TODO: Integrate with Product Service via Feign/RestTemplate
        // For now, return a default value
        log.debug("Checking inventory for product: {}, variant: {}", productId, variantId);
        return 100;
    }

    @Override
    public int getMaxAllowedQuantity(Long productId, Long variantId) {
        // TODO: Integrate with Product Service via Feign/RestTemplate
        // For now, return a default value
        return DEFAULT_MAX_QUANTITY;
    }
}