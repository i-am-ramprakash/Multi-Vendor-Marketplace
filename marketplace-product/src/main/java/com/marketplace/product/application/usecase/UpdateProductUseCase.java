package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.ProductResponse;
import com.marketplace.product.application.dto.UpdateProductRequest;
import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.exception.InvalidProductStateException;
import com.marketplace.product.domain.exception.ProductNotFoundException;
import com.marketplace.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse execute(Long productId, Long vendorId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.isOwner(vendorId)) {
            throw new SecurityException("You are not authorized to update this product");
        }

        if (!product.canBeEdited()) {
            throw new InvalidProductStateException(product.getStatus().name(), "update");
        }

        product.updateBasicInfo(
            request.getName(),
            request.getDescription(),
            request.getShortDescription(),
            request.getBasePrice()
        );

        product.updatePricing(
            request.getBasePrice(),
            request.getCompareAtPrice(),
            request.getCostPrice()
        );

        if (request.getSku() != null) {
            product.setSku(com.marketplace.product.domain.valueobject.SKU.of(request.getSku()));
        }
        if (request.getBarcode() != null) {
            product.setBarcode(request.getBarcode());
        }

        product.updateShipping(
            request.getWeight(),
            request.getDimensions(),
            request.getRequiresShipping()
        );

        if (request.getIsDigital() != null) {
            product.setDigital(request.getIsDigital());
        }
        if (request.getRequiresShipping() != null) {
            product.setRequiresShipping(request.getRequiresShipping());
        }
        if (request.getTaxClass() != null) {
            product.setTaxClass(request.getTaxClass());
        }

        product.updateSEO(
            request.getMetaTitle(),
            request.getMetaDescription(),
            request.getMetaKeywords()
        );

        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }
}