package com.marketplace.product.application.service;

import com.marketplace.product.application.dto.*;
import com.marketplace.product.application.usecase.*;
import com.marketplace.product.domain.entity.*;
import com.marketplace.product.domain.exception.*;
import com.marketplace.product.domain.repository.*;
import com.marketplace.product.domain.valueobject.ProductStatus;
import com.marketplace.product.domain.valueobject.SKU;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final SearchProductsUseCase searchProductsUseCase;
    private final SubmitProductForApprovalUseCase submitProductForApprovalUseCase;
    private final ApproveProductUseCase approveProductUseCase;
    private final RejectProductUseCase rejectProductUseCase;
    private final UpdateInventoryUseCase updateInventoryUseCase;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductImageRepository imageRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        return createProductUseCase.execute(request);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, Long vendorId, UpdateProductRequest request) {
        return updateProductUseCase.execute(productId, vendorId, request);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {
        return getProductUseCase.execute(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySlug(String slug) {
        return getProductUseCase.executeBySlug(slug);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId, Long vendorId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.isOwner(vendorId)) {
            throw new SecurityException("You are not authorized to delete this product");
        }

        if (!product.canBeEdited()) {
            throw new InvalidProductStateException(product.getStatus().name(), "delete");
        }

        productRepository.delete(product);
    }

    @Override
    @Transactional
    public void publishProduct(Long productId, Long vendorId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.isOwner(vendorId)) {
            throw new SecurityException("You are not authorized to publish this product");
        }

        product.submitForApproval();
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void unpublishProduct(Long productId, Long vendorId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.isOwner(vendorId)) {
            throw new SecurityException("You are not authorized to unpublish this product");
        }

        product.unpublish();
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListResponse searchProducts(ProductSearchRequest request) {
        return searchProductsUseCase.execute(request);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListResponse getVendorProducts(Long vendorId, int page, int size) {
        ProductSearchRequest request = ProductSearchRequest.builder()
            .vendorId(vendorId)
            .page(page)
            .size(size)
            .sortBy("createdAt")
            .sortDirection("DESC")
            .build();
        return searchProductsUseCase.execute(request);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListResponse getCategoryProducts(Long categoryId, int page, int size) {
        ProductSearchRequest request = ProductSearchRequest.builder()
            .categoryId(categoryId)
            .page(page)
            .size(size)
            .sortBy("createdAt")
            .sortDirection("DESC")
            .build();
        return searchProductsUseCase.execute(request);
    }

    @Override
    @Transactional
    public ProductApprovalResponse submitForApproval(Long productId, Long vendorId) {
        return submitProductForApprovalUseCase.execute(productId, vendorId);
    }

    @Override
    @Transactional
    public ProductResponse approveProduct(Long productId, Long approvedBy, String adminNotes) {
        return approveProductUseCase.execute(productId, approvedBy, adminNotes);
    }

    @Override
    @Transactional
    public ProductResponse rejectProduct(Long productId, Long rejectedBy, String rejectionReason) {
        return rejectProductUseCase.execute(productId, rejectedBy, rejectionReason);
    }

    @Override
    @Transactional
    public VariantResponse updateInventory(Long variantId, UpdateInventoryRequest request) {
        return updateInventoryUseCase.execute(variantId, request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VariantResponse> getVariants(Long productId) {
        return variantRepository.findByProductId(productId).stream()
            .map(VariantResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VariantResponse addVariant(Long productId, CreateVariantRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVariant variant = new ProductVariant(
            request.getName(),
            request.getPrice(),
            request.getSku() != null ? SKU.of(request.getSku()) : null
        );
        variant.updatePrice(
            request.getPrice(),
            request.getCompareAtPrice(),
            request.getCostPrice()
        );
        variant.updateInventory(request.getInventoryQuantity() != null ? request.getInventoryQuantity() : 0);
        if (request.getLowStockThreshold() != null) {
            variant.setLowStockThreshold(request.getLowStockThreshold());
        }
        variant.setTrackInventory(request.getTrackInventory() != null ? request.getTrackInventory() : true);
        variant.setAllowBackorder(request.getAllowBackorder() != null ? request.getAllowBackorder() : false);
        if (request.getImageUrl() != null) {
            variant.setImageUrl(request.getImageUrl());
        }
        if (request.getPosition() != null) {
            variant.setPosition(request.getPosition());
        }
        variant.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        product.addVariant(variant);
        ProductVariant savedVariant = variantRepository.save(variant);

        return VariantResponse.from(savedVariant);
    }

    @Override
    @Transactional
    public VariantResponse updateVariant(Long variantId, CreateVariantRequest request) {
        ProductVariant variant = variantRepository.findById(variantId)
            .orElseThrow(() -> new VariantNotFoundException(variantId));

        variant.setName(request.getName());
        variant.updatePrice(
            request.getPrice(),
            request.getCompareAtPrice(),
            request.getCostPrice()
        );
        if (request.getSku() != null) {
            variant.setSku(SKU.of(request.getSku()));
        }
        if (request.getBarcode() != null) {
            variant.setBarcode(request.getBarcode());
        }
        variant.updateInventory(request.getInventoryQuantity() != null ? request.getInventoryQuantity() : variant.getInventoryQuantity());

        ProductVariant savedVariant = variantRepository.save(variant);

        return VariantResponse.from(savedVariant);
    }

    @Override
    @Transactional
    public void deleteVariant(Long variantId, Long vendorId) {
        ProductVariant variant = variantRepository.findById(variantId)
            .orElseThrow(() -> new VariantNotFoundException(variantId));

        if (!variant.getProduct().isOwner(vendorId)) {
            throw new SecurityException("You are not authorized to delete this variant");
        }

        variantRepository.delete(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageResponse> getImages(Long productId) {
        return imageRepository.findByProductIdOrderByPositionAsc(productId).stream()
            .map(ImageResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ImageResponse addImage(Long productId, CreateImageRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductVariant variant = null;
        if (request.getVariantId() != null) {
            variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new VariantNotFoundException(request.getVariantId()));
        }

        ProductImage image = new ProductImage(
            request.getUrl(),
            request.getAltText(),
            request.getPosition() != null ? request.getPosition() : 0,
            request.getIsPrimary() != null ? request.getIsPrimary() : false
        );

        if (variant != null) {
            image.setVariant(variant);
        }

        product.addImage(image);
        ProductImage savedImage = imageRepository.save(image);

        return ImageResponse.from(savedImage);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId, Long vendorId) {
        ProductImage image = imageRepository.findById(imageId)
            .orElseThrow(() -> new ProductNotFoundException(imageId));

        if (!image.getProduct().isOwner(vendorId)) {
            throw new SecurityException("You are not authorized to delete this image");
        }

        imageRepository.delete(image);
    }

    @Override
    @Transactional
    public void reorderImages(Long productId, List<Long> imageIds) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        List<ProductImage> images = imageRepository.findByProductId(productId);

        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            final int index = i;
            images.stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .ifPresent(img -> img.setPosition(index));
        }

        images.forEach(imageRepository::save);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryResponse request) {
        Category category = new Category(request.getName(), request.getSlug());

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getParentId()));
            category.setParent(parent);
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getImageUrl() != null) {
            category.setImageUrl(request.getImageUrl());
        }
        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }

        Category savedCategory = categoryRepository.save(category);

        return CategoryResponse.from(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryResponse request) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getSlug() != null) {
            category.setSlug(request.getSlug());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getImageUrl() != null) {
            category.setImageUrl(request.getImageUrl());
        }
        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getIsActive() != null) {
            if (request.getIsActive()) {
                category.activate();
            } else {
                category.deactivate();
            }
        }

        Category savedCategory = categoryRepository.save(category);

        return CategoryResponse.from(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return CategoryResponse.from(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getRootCategories() {
        return categoryRepository.findByParentIsNullOrderByDisplayOrderAsc().stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getChildCategories(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        return categoryRepository.findByParentIdOrderByDisplayOrderAsc(categoryId).stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoryAncestors(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        List<Category> ancestors = category.getAncestors();
        return ancestors.stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
            .map(CategoryResponse::fromWithChildren)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListResponse getPendingProducts(int page, int size) {
        ProductSearchRequest request = ProductSearchRequest.builder()
            .status(ProductStatus.PENDING_APPROVAL.name())
            .page(page)
            .size(size)
            .sortBy("createdAt")
            .sortDirection("DESC")
            .build();
        return searchProductsUseCase.execute(request);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListResponse getRejectedProducts(int page, int size) {
        ProductSearchRequest request = ProductSearchRequest.builder()
            .status(ProductStatus.REJECTED.name())
            .page(page)
            .size(size)
            .sortBy("createdAt")
            .sortDirection("DESC")
            .build();
        return searchProductsUseCase.execute(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductApprovalResponse> getApprovalRequests() {
        return productRepository.findByStatus(ProductStatus.PENDING_APPROVAL).stream()
            .map(product -> ProductApprovalResponse.builder()
                .productId(product.getId())
                .vendorId(product.getVendorId())
                .productName(product.getName())
                .status(product.getStatus().name())
                .build())
            .collect(Collectors.toList());
    }
}