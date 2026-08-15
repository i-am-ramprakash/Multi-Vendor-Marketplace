package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.ProductResponse;
import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.entity.ProductApprovalRequest;
import com.marketplace.product.domain.event.ProductApprovedEvent;
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
public class ApproveProductUseCase {

    private final ProductRepository productRepository;
    private final ProductApprovalRequestRepository approvalRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ProductResponse execute(Long productId, Long approvedBy, String adminNotes) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.getStatus().canTransitionTo(com.marketplace.product.domain.valueobject.ProductStatus.APPROVED)) {
            throw new InvalidProductStateException(product.getStatus().name(), "approve");
        }

        product.approve(approvedBy);
        Product savedProduct = productRepository.save(product);

        approvalRequestRepository.findByProductIdAndStatus(
            productId,
            ProductApprovalRequest.ApprovalStatus.PENDING
        ).stream()
        .findFirst()
        .ifPresent(request -> {
            request.approve(approvedBy, adminNotes);
            approvalRequestRepository.save(request);
        });

        eventPublisher.publishEvent(new ProductApprovedEvent(
            savedProduct.getId(),
            savedProduct.getVendorId(),
            savedProduct.getName(),
            approvedBy
        ));

        return ProductResponse.from(savedProduct);
    }
}