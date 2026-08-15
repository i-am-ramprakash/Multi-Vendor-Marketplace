package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.ProductResponse;
import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.exception.ProductNotFoundException;
import com.marketplace.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetProductUseCase {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductResponse execute(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        product.incrementViewCount();
        productRepository.save(product);

        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse executeBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
            .orElseThrow(() -> new ProductNotFoundException("slug: " + slug));

        product.incrementViewCount();
        productRepository.save(product);

        return ProductResponse.from(product);
    }
}