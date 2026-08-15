package com.marketplace.order.infrastructure.service;

import com.marketplace.order.domain.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Override
    public boolean checkAvailability(Long productId, Long variantId, int quantity) {
        // TODO: Integrate with Product Service via Feign/RestTemplate
        log.debug("Checking availability for product: {}, variant: {}, quantity: {}", productId, variantId, quantity);
        return true;
    }

    @Override
    public void reserveInventory(Long productId, Long variantId, int quantity, Long orderId) {
        // TODO: Integrate with Product Service via Feign/RestTemplate
        log.debug("Reserving inventory for product: {}, variant: {}, quantity: {}, order: {}", productId, variantId, quantity, orderId);
    }

    @Override
    public void releaseInventory(Long productId, Long variantId, int quantity, Long orderId) {
        // TODO: Integrate with Product Service via Feign/RestTemplate
        log.debug("Releasing inventory for product: {}, variant: {}, quantity: {}, order: {}", productId, variantId, quantity, orderId);
    }

    @Override
    public void deductInventory(Long productId, Long variantId, int quantity, Long orderId) {
        // TODO: Integrate with Product Service via Feign/RestTemplate
        log.debug("Deducting inventory for product: {}, variant: {}, quantity: {}, order: {}", productId, variantId, quantity, orderId);
    }
}