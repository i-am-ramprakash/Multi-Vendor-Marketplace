package com.marketplace.product.application.usecase;

import com.marketplace.product.application.dto.CreateProductRequest;
import com.marketplace.product.application.dto.ProductResponse;
import com.marketplace.product.domain.entity.Category;
import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.event.ProductCreatedEvent;
import com.marketplace.product.domain.exception.CategoryNotFoundException;
import com.marketplace.product.domain.exception.ProductAlreadyExistsException;
import com.marketplace.product.domain.repository.CategoryRepository;
import com.marketplace.product.domain.repository.ProductRepository;
import com.marketplace.product.domain.valueobject.ProductSlug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private com.marketplace.product.domain.repository.ProductVariantRepository variantRepository;

    @Mock
    private com.marketplace.product.domain.repository.ProductImageRepository imageRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateProductUseCase(productRepository, categoryRepository, variantRepository, imageRepository, eventPublisher);
    }

    @Test
    void execute_WithValidRequest_ShouldCreateProductAndReturnResponse() {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
            .vendorId(1L)
            .categoryId(1L)
            .name("Wireless Headphones")
            .slug("wireless-headphones")
            .description("High-quality wireless headphones")
            .basePrice(new BigDecimal("99.99"))
            .sku("WH-001")
            .build();

        Category category = new Category("Electronics", "electronics");
        setField(category, "id", 1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.existsBySlug(any(String.class))).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ProductResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getVendorId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Wireless Headphones");
        assertThat(response.getSlug()).isEqualTo("wireless-headphones");
        assertThat(response.getBasePrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(response.getStatus()).isEqualTo(com.marketplace.product.domain.valueobject.ProductStatus.DRAFT);

        verify(productRepository).save(any(Product.class));
        verify(eventPublisher).publishEvent(any(ProductCreatedEvent.class));
    }

    @Test
    void execute_WithExistingSlug_ShouldThrowProductAlreadyExistsException() {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
            .vendorId(1L)
            .categoryId(1L)
            .name("Wireless Headphones")
            .slug("wireless-headphones")
            .basePrice(new BigDecimal("99.99"))
            .build();

        Category category = new Category("Electronics", "electronics");
        setField(category, "id", 1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.existsBySlug(any(String.class))).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(ProductAlreadyExistsException.class)
            .hasMessageContaining("wireless-headphones");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void execute_WithInvalidCategoryId_ShouldThrowCategoryNotFoundException() {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
            .vendorId(1L)
            .categoryId(999L)
            .name("Wireless Headphones")
            .slug("wireless-headphones")
            .basePrice(new BigDecimal("99.99"))
            .build();

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining("999");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void execute_WithMinimalFields_ShouldCreateProductWithDefaults() {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
            .vendorId(1L)
            .categoryId(1L)
            .name("Wireless Headphones")
            .basePrice(new BigDecimal("99.99"))
            .build();

        Category category = new Category("Electronics", "electronics");
        setField(category, "id", 1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.existsBySlug(any(String.class))).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ProductResponse response = useCase.execute(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Wireless Headphones");
        assertThat(response.getStatus()).isEqualTo(com.marketplace.product.domain.valueobject.ProductStatus.DRAFT);
        assertThat(response.getIsFeatured()).isFalse();
        assertThat(response.getIsDigital()).isFalse();
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