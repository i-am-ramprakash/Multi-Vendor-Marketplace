package com.marketplace.cart.application.usecase;

import com.marketplace.cart.application.dto.AddToCartRequest;
import com.marketplace.cart.application.dto.CartItemResponse;
import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.entity.CartItem;
import com.marketplace.cart.domain.event.ItemAddedToCartEvent;
import com.marketplace.cart.domain.exception.InsufficientInventoryException;
import com.marketplace.cart.domain.repository.CartRepository;
import com.marketplace.cart.domain.service.InventoryValidationService;
import com.marketplace.cart.domain.valueobject.CartStatus;
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
class AddToCartUseCaseTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private InventoryValidationService inventoryValidationService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private AddToCartUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddToCartUseCase(cartRepository, inventoryValidationService, eventPublisher);
    }

    @Test
    void execute_WithValidRequest_ShouldAddItemToCart() {
        // Given
        Long userId = 1L;
        AddToCartRequest request = AddToCartRequest.builder()
            .productId(100L)
            .variantId(200L)
            .productName("Wireless Headphones")
            .variantName("Black")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(2)
            .imageUrl("https://example.com/image.jpg")
            .build();

        when(inventoryValidationService.isProductAvailable(100L, 200L, 2)).thenReturn(true);
        when(inventoryValidationService.getMaxAllowedQuantity(100L, 200L)).thenReturn(10);
        when(cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            CartItem item = cart.getItems().get(0);
            setField(item, "id", 1L);
            setField(cart, "id", 1L);
            return cart;
        });

        // When
        CartItemResponse response = useCase.execute(userId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(100L);
        assertThat(response.getVariantId()).isEqualTo(200L);
        assertThat(response.getProductName()).isEqualTo("Wireless Headphones");
        assertThat(response.getQuantity()).isEqualTo(2);
        assertThat(response.getUnitPrice()).isEqualByComparingTo(new BigDecimal("99.99"));

        verify(cartRepository).save(any(Cart.class));
        verify(eventPublisher).publishEvent(any(ItemAddedToCartEvent.class));
    }

    @Test
    void execute_WithExistingCartItem_ShouldUpdateQuantity() {
        // Given
        Long userId = 1L;
        AddToCartRequest request = AddToCartRequest.builder()
            .productId(100L)
            .variantId(200L)
            .productName("Wireless Headphones")
            .variantName("Black")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(2)
            .build();

        Cart existingCart = new Cart(userId);
        setField(existingCart, "id", 1L);
        CartItem existingItem = existingCart.addItem(100L, 200L, "Wireless Headphones", "Black",
                                                     new BigDecimal("99.99"), 1, null);
        setField(existingItem, "id", 1L);

        when(inventoryValidationService.isProductAvailable(100L, 200L, 3)).thenReturn(true);
        when(cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CartItemResponse response = useCase.execute(userId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getQuantity()).isEqualTo(3);

        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void execute_WithInsufficientInventory_ShouldThrowException() {
        // Given
        Long userId = 1L;
        AddToCartRequest request = AddToCartRequest.builder()
            .productId(100L)
            .variantId(200L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(5)
            .build();

        when(inventoryValidationService.isProductAvailable(100L, 200L, 5)).thenReturn(false);
        when(inventoryValidationService.getAvailableQuantity(100L, 200L)).thenReturn(2);

        // When & Then
        assertThatThrownBy(() -> useCase.execute(userId, request))
            .isInstanceOf(InsufficientInventoryException.class)
            .hasMessageContaining("Wireless Headphones");

        verify(cartRepository, never()).save(any(Cart.class));
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