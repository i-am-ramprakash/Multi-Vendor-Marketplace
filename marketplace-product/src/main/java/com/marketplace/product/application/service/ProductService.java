package com.marketplace.product.application.service;

import com.marketplace.product.application.dto.*;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse updateProduct(Long productId, Long vendorId, UpdateProductRequest request);

    ProductResponse getProduct(Long productId);

    ProductResponse getProductBySlug(String slug);

    void deleteProduct(Long productId, Long vendorId);

    void publishProduct(Long productId, Long vendorId);

    void unpublishProduct(Long productId, Long vendorId);

    ProductListResponse searchProducts(ProductSearchRequest request);

    ProductListResponse getVendorProducts(Long vendorId, int page, int size);

    ProductListResponse getCategoryProducts(Long categoryId, int page, int size);

    ProductApprovalResponse submitForApproval(Long productId, Long vendorId);

    ProductResponse approveProduct(Long productId, Long approvedBy, String adminNotes);

    ProductResponse rejectProduct(Long productId, Long rejectedBy, String rejectionReason);

    VariantResponse updateInventory(Long variantId, UpdateInventoryRequest request);

    List<VariantResponse> getVariants(Long productId);

    VariantResponse addVariant(Long productId, CreateVariantRequest request);

    VariantResponse updateVariant(Long variantId, CreateVariantRequest request);

    void deleteVariant(Long variantId, Long vendorId);

    List<ImageResponse> getImages(Long productId);

    ImageResponse addImage(Long productId, CreateImageRequest request);

    void deleteImage(Long imageId, Long vendorId);

    void reorderImages(Long productId, List<Long> imageIds);

    CategoryResponse createCategory(CategoryResponse request);

    CategoryResponse updateCategory(Long categoryId, CategoryResponse request);

    void deleteCategory(Long categoryId);

    CategoryResponse getCategory(Long categoryId);

    List<CategoryResponse> getCategories();

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getRootCategories();

    List<CategoryResponse> getChildCategories(Long categoryId);

    List<CategoryResponse> getCategoryAncestors(Long categoryId);

    List<CategoryResponse> getCategoryTree();

    ProductListResponse getPendingProducts(int page, int size);

    ProductListResponse getRejectedProducts(int page, int size);

    List<ProductApprovalResponse> getApprovalRequests();
}