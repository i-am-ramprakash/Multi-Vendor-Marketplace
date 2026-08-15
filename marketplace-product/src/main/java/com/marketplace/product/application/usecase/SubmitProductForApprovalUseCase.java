package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.ProductApprovalResponse;
import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.entity.ProductApprovalRequest;
import com.marketplace.product.domain.exception.InvalidProductStateException;
import com.marketplace.product.domain.exception.ProductNotFoundException;
import com.marketplace.product.domain.repository.ProductApprovalRequestRepository;
import com.marketplace.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmitProductForApprovalUseCase {

    private final ProductRepository productRepository;
    private final ProductApprovalRequestRepository approvalRequestRepository;

    @Transactional
    public ProductApprovalResponse execute(Long productId, Long vendorId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.isOwner(vendorId)) {
            throw new SecurityException("You are not authorized to submit this product for approval");
        }

        if (!product.getStatus().canTransitionTo(com.marketplace.product.domain.valueobject.ProductStatus.PENDING_APPROVAL)) {
            throw new InvalidProductStateException(product.getStatus().name(), "submit for approval");
        }

        product.submitForApproval();
        productRepository.save(product);

        ProductApprovalRequest approvalRequest = new ProductApprovalRequest(
            product,
            vendorId,
            ProductApprovalRequest.RequestType.NEW_PRODUCT
        );

        ProductApprovalRequest savedRequest = approvalRequestRepository.save(approvalRequest);

        return ProductApprovalResponse.from(savedRequest);
    }
}