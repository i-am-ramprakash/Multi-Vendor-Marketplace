package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.*;
import com.marketplace.product.domain.entity.*;
import com.marketplace.product.domain.event.ProductCreatedEvent;
import com.marketplace.product.domain.exception.*;
import com.marketplace.product.domain.repository.*;
import com.marketplace.product.domain.valueobject.ProductSlug;
import com.marketplace.product.domain.valueobject.ProductStatus;
import com.marketplace.product.domain.valueobject.SKU;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductImageRepository imageRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ProductResponse execute(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

        ProductSlug slug = request.getSlug() != null 
            ? ProductSlug.of(request.getSlug())
            : ProductSlug.fromProductName(request.getName());

        if (productRepository.existsBySlug(slug.getValue())) {
            throw new ProductAlreadyExistsException("slug", slug.getValue());
        }

        Product product = new Product(
            request.getVendorId(),
            category,
            request.getName(),
            slug,
            request.getBasePrice()
        );

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
            product.setSku(SKU.of(request.getSku()));
        }
        if (request.getBarcode() != null) {
            product.setBarcode(request.getBarcode());
        }

        product.updateShipping(
            request.getWeight(),
            request.getDimensions(),
            request.getRequiresShipping()
        );

        product.setDigital(request.getIsDigital() != null ? request.getIsDigital() : false);
        product.setRequiresShipping(request.getRequiresShipping() != null ? request.getRequiresShipping() : true);

        if (request.getTaxClass() != null) {
            product.setTaxClass(request.getTaxClass());
        }

        product.updateSEO(
            request.getMetaTitle(),
            request.getMetaDescription(),
            request.getMetaKeywords()
        );

        Product savedProduct = productRepository.save(product);

        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            for (CreateVariantRequest variantRequest : request.getVariants()) {
                ProductVariant variant = new ProductVariant(
                    variantRequest.getName(),
                    variantRequest.getPrice(),
                    variantRequest.getSku() != null ? SKU.of(variantRequest.getSku()) : null
                );
                variant.updatePrice(
                    variantRequest.getPrice(),
                    variantRequest.getCompareAtPrice(),
                    variantRequest.getCostPrice()
                );
                variant.updateInventory(variantRequest.getInventoryQuantity() != null ? variantRequest.getInventoryQuantity() : 0);
                if (variantRequest.getLowStockThreshold() != null) {
                    variant.setLowStockThreshold(variantRequest.getLowStockThreshold());
                }
                variant.setTrackInventory(variantRequest.getTrackInventory() != null ? variantRequest.getTrackInventory() : true);
                variant.setAllowBackorder(variantRequest.getAllowBackorder() != null ? variantRequest.getAllowBackorder() : false);
                if (variantRequest.getImageUrl() != null) {
                    variant.setImageUrl(variantRequest.getImageUrl());
                }
                if (variantRequest.getPosition() != null) {
                    variant.setPosition(variantRequest.getPosition());
                }
                variant.setIsActive(variantRequest.getIsActive() != null ? variantRequest.getIsActive() : true);

                savedProduct.addVariant(variant);
                variantRepository.save(variant);
            }
        }

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (CreateImageRequest imageRequest : request.getImages()) {
                ProductImage image = new ProductImage(
                    imageRequest.getUrl(),
                    imageRequest.getAltText(),
                    imageRequest.getPosition() != null ? imageRequest.getPosition() : 0,
                    imageRequest.getIsPrimary() != null ? imageRequest.getIsPrimary() : false
                );
                savedProduct.addImage(image);
                imageRepository.save(image);
            }
        }

        eventPublisher.publishEvent(new ProductCreatedEvent(
            savedProduct.getId(),
            savedProduct.getVendorId(),
            savedProduct.getName(),
            savedProduct.getSlug().getValue()
        ));

        return ProductResponse.from(savedProduct);
    }
}