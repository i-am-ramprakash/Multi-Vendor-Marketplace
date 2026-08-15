package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.ProductResponse;
import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.entity.ProductApprovalRequest;
import com.marketplace.product.domain.event.ProductRejectedEvent;
import com.marketplace.product.domain.exception.InvalidProductStateException;
import com.marketplace.product.domain.exception.ProductNotFoundException;
import com.marketplace.product.domain.repository.ProductApprovalRequestRepository;
import com.marketplace.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RejectProductUseCase {

    private final ProductRepository productRepository;
    private final ProductApprovalRequestRepository approvalRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ProductResponse execute(Long productId, Long rejectedBy, String rejectionReason) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.getStatus().canTransitionTo(com.marketplace.product.domain.valueobject.ProductStatus.REJECTED)) {
            throw new InvalidProductStateException(product.getStatus().name(), "reject");
        }

        product.reject(rejectedBy, rejectionReason);
        Product savedProduct = productRepository.save(product);

        approvalRequestRepository.findByProductIdAndStatus(
            productId,
            ProductApprovalRequest.ApprovalStatus.PENDING
        ).stream()
        .findFirst()
        .ifPresent(request -> {
            request.reject(rejectedBy, rejectionReason);
            approvalRequestRepository.save(request);
        });

        eventPublisher.publishEvent(new ProductRejectedEvent(
            savedProduct.getId(),
            savedProduct.getVendorId(),
            savedProduct.getName(),
            rejectedBy,
            rejectionReason
        ));

        return ProductResponse.from(savedProduct);
    }
}