package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.ProductListResponse;
import com.marketplace.product.application.dto.ProductResponse;
import com.marketplace.product.application.dto.ProductSearchRequest;
import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.repository.ProductRepository;
import com.marketplace.product.domain.valueobject.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchProductsUseCase {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductListResponse execute(ProductSearchRequest request) {
        Sort sort = createSort(request.getSortBy(), request.getSortDirection());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Product> productPage;

        if (hasSearchCriteria(request)) {
            ProductStatus status = null;
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                try {
                    status = ProductStatus.valueOf(request.getStatus().toUpperCase());
                } catch (IllegalArgumentException e) {
                    status = null;
                }
            }

            productPage = productRepository.search(
                request.getKeyword(),
                request.getCategoryId(),
                request.getVendorId(),
                status,
                pageable
            );
        } else if (request.getCategoryId() != null) {
            ProductStatus status = null;
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                try {
                    status = ProductStatus.valueOf(request.getStatus().toUpperCase());
                } catch (IllegalArgumentException e) {
                    status = null;
                }
            }
            if (status != null) {
                productPage = productRepository.findByCategoryIdAndStatus(
                    request.getCategoryId(),
                    status,
                    pageable
                );
            } else {
                productPage = productRepository.findByStatus(ProductStatus.APPROVED, pageable);
            }
        } else {
            productPage = productRepository.findByStatus(ProductStatus.APPROVED, pageable);
        }

        java.util.List<ProductResponse> productResponses = productPage.getContent().stream()
            .map(ProductResponse::from)
            .toList();

        return ProductListResponse.builder()
            .products(productResponses)
            .totalElements(productPage.getTotalElements())
            .totalPages(productPage.getTotalPages())
            .currentPage(productPage.getNumber())
            .pageSize(productPage.getSize())
            .hasNext(productPage.hasNext())
            .hasPrevious(productPage.hasPrevious())
            .sortBy(request.getSortBy())
            .sortDirection(request.getSortDirection())
            .build();
    }

    private boolean hasSearchCriteria(ProductSearchRequest request) {
        return request.getKeyword() != null && !request.getKeyword().isEmpty()
            || request.getVendorId() != null;
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;

        String field = switch (sortBy != null ? sortBy.toLowerCase() : "createdat") {
            case "name", "productname" -> "name";
            case "price" -> "basePrice";
            case "created" -> "createdAt";
            case "updated" -> "updatedAt";
            case "sold" -> "totalSold";
            case "rating" -> "averageRating";
            case "reviews" -> "reviewCount";
            case "views" -> "viewCount";
            default -> "createdAt";
        };

        return Sort.by(direction, field);
    }
}