package com.marketplace.product.application.service;

import com.marketplace.product.application.dto.*;
import com.marketplace.product.application.usecase.*;
import com.marketplace.product.domain.entity.Category;
import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.entity.ProductVariant;
import com.marketplace.product.domain.exception.CategoryNotFoundException;
import com.marketplace.product.domain.repository.*;
import com.marketplace.product.domain.valueobject.ProductSlug;
import com.marketplace.product.domain.valueobject.ProductStatus;
import com.marketplace.product.domain.valueobject.SKU;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private CreateProductUseCase createProductUseCase;

    @Mock
    private UpdateProductUseCase updateProductUseCase;

    @Mock
    private GetProductUseCase getProductUseCase;

    @Mock
    private SearchProductsUseCase searchProductsUseCase;

    @Mock
    private SubmitProductForApprovalUseCase submitProductForApprovalUseCase;

    @Mock
    private ApproveProductUseCase approveProductUseCase;

    @Mock
    private RejectProductUseCase rejectProductUseCase;

    @Mock
    private UpdateInventoryUseCase updateInventoryUseCase;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductVariantRepository variantRepository;

    @Mock
    private ProductImageRepository imageRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(
            createProductUseCase,
            updateProductUseCase,
            getProductUseCase,
            searchProductsUseCase,
            submitProductForApprovalUseCase,
            approveProductUseCase,
            rejectProductUseCase,
            updateInventoryUseCase,
            productRepository,
            variantRepository,
            imageRepository,
            categoryRepository
        );
    }

    @Test
    void createProduct_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
            .vendorId(1L)
            .categoryId(1L)
            .name("Wireless Headphones")
            .basePrice(new BigDecimal("99.99"))
            .build();

        ProductResponse expectedResponse = ProductResponse.builder()
            .id(1L)
            .vendorId(1L)
            .name("Wireless Headphones")
            .basePrice(new BigDecimal("99.99"))
            .status(ProductStatus.DRAFT)
            .build();

        when(createProductUseCase.execute(request)).thenReturn(expectedResponse);

        // When
        ProductResponse response = productService.createProduct(request);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(createProductUseCase).execute(request);
    }

    @Test
    void getProduct_WithExistingId_ShouldDelegateToUseCase() {
        // Given
        ProductResponse expectedResponse = ProductResponse.builder()
            .id(1L)
            .name("Wireless Headphones")
            .build();

        when(getProductUseCase.execute(1L)).thenReturn(expectedResponse);

        // When
        ProductResponse response = productService.getProduct(1L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getProductUseCase).execute(1L);
    }

    @Test
    void deleteProduct_WithValidOwner_ShouldDeleteProduct() {
        // Given
        Product product = new Product(1L, null, "Wireless Headphones", ProductSlug.of("wireless-headphones"), new BigDecimal("99.99"));
        setField(product, "id", 1L);
        product.setStatus(ProductStatus.DRAFT);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        productService.deleteProduct(1L, 1L);

        // Then
        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_WithInvalidOwner_ShouldThrowSecurityException() {
        // Given
        Product product = new Product(1L, null, "Wireless Headphones", ProductSlug.of("wireless-headphones"), new BigDecimal("99.99"));
        setField(product, "id", 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When & Then
        assertThatThrownBy(() -> productService.deleteProduct(1L, 2L))
            .isInstanceOf(SecurityException.class)
            .hasMessageContaining("not authorized");
    }

    @Test
    void getVariants_WithExistingProductId_ShouldReturnVariants() {
        // Given
        ProductVariant variant1 = new ProductVariant("Small", new BigDecimal("99.99"), SKU.of("WH-S"));
        ProductVariant variant2 = new ProductVariant("Large", new BigDecimal("109.99"), SKU.of("WH-L"));
        List<ProductVariant> variants = Arrays.asList(variant1, variant2);

        when(variantRepository.findByProductId(1L)).thenReturn(variants);

        // When
        List<VariantResponse> response = productService.getVariants(1L);

        // Then
        assertThat(response).hasSize(2);
        verify(variantRepository).findByProductId(1L);
    }

    @Test
    void addVariant_WithValidRequest_ShouldAddVariantToProduct() {
        // Given
        Product product = new Product(1L, null, "Wireless Headphones", ProductSlug.of("wireless-headphones"), new BigDecimal("99.99"));
        setField(product, "id", 1L);

        CreateVariantRequest request = CreateVariantRequest.builder()
            .name("Small")
            .price(new BigDecimal("99.99"))
            .sku("WH-S")
            .inventoryQuantity(10)
            .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(variantRepository.save(any(ProductVariant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        VariantResponse response = productService.addVariant(1L, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Small");
        verify(variantRepository).save(any(ProductVariant.class));
    }

    @Test
    void getCategories_WithActiveCategories_ShouldReturnCategories() {
        // Given
        Category category1 = new Category("Electronics", "electronics");
        Category category2 = new Category("Clothing", "clothing");
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(categories);

        // When
        List<CategoryResponse> response = productService.getCategories();

        // Then
        assertThat(response).hasSize(2);
        verify(categoryRepository).findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    @Test
    void getChildCategories_WithExistingParentId_ShouldReturnChildCategories() {
        // Given
        Category parent = new Category("Electronics", "electronics");
        setField(parent, "id", 1L);

        Category child1 = new Category("Phones", "phones");
        Category child2 = new Category("Laptops", "laptops");
        List<Category> children = Arrays.asList(child1, child2);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(categoryRepository.findByParentIdOrderByDisplayOrderAsc(1L)).thenReturn(children);

        // When
        List<CategoryResponse> response = productService.getChildCategories(1L);

        // Then
        assertThat(response).hasSize(2);
        verify(categoryRepository).findByParentIdOrderByDisplayOrderAsc(1L);
    }

    @Test
    void getChildCategories_WithInvalidParentId_ShouldThrowCategoryNotFoundException() {
        // Given
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getChildCategories(999L))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining("999");
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Cannot set field: " + fieldName, e);
        }
    }
}