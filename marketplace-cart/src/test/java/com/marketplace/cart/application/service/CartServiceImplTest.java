package com.marketplace.cart.application.service;

import com.marketplace.cart.application.dto.*;
import com.marketplace.cart.application.usecase.*;
import com.marketplace.cart.domain.entity.Cart;
import com.marketplace.cart.domain.entity.CartItem;
import com.marketplace.cart.domain.exception.CartNotFoundException;
import com.marketplace.cart.domain.repository.CartRepository;
import com.marketplace.cart.domain.service.InventoryValidationService;
import com.marketplace.cart.domain.valueobject.CartStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private AddToCartUseCase addToCartUseCase;

    @Mock
    private RemoveFromCartUseCase removeFromCartUseCase;

    @Mock
    private RemoveCartItemByIdUseCase removeCartItemByIdUseCase;

    @Mock
    private UpdateCartItemUseCase updateCartItemUseCase;

    @Mock
    private GetCartUseCase getCartUseCase;

    @Mock
    private ClearCartUseCase clearCartUseCase;

    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl(
            addToCartUseCase,
            removeFromCartUseCase,
            removeCartItemByIdUseCase,
            updateCartItemUseCase,
            getCartUseCase,
            clearCartUseCase
        );
    }

    @Test
    void addToCart_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        Long userId = 1L;
        AddToCartRequest request = AddToCartRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(1)
            .build();

        CartItemResponse expectedResponse = CartItemResponse.builder()
            .id(1L)
            .productId(100L)
            .productName("Wireless Headphones")
            .quantity(1)
            .unitPrice(new BigDecimal("99.99"))
            .build();

        when(addToCartUseCase.execute(userId, request)).thenReturn(expectedResponse);

        // When
        CartItemResponse response = cartService.addToCart(userId, request);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(addToCartUseCase).execute(userId, request);
    }

    @Test
    void removeFromCart_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        Long userId = 1L;
        Long productId = 100L;
        Long variantId = 200L;

        // When
        cartService.removeFromCart(userId, productId, variantId);

        // Then
        verify(removeFromCartUseCase).execute(userId, productId, variantId);
    }

    @Test
    void getCart_WithExistingCart_ShouldDelegateToUseCase() {
        // Given
        Long userId = 1L;
        CartResponse expectedResponse = CartResponse.builder()
            .id(1L)
            .userId(userId)
            .itemCount(2)
            .subtotal(new BigDecimal("199.98"))
            .total(new BigDecimal("199.98"))
            .build();

        when(getCartUseCase.execute(userId)).thenReturn(expectedResponse);

        // When
        CartResponse response = cartService.getCart(userId);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getCartUseCase).execute(userId);
    }

    @Test
    void getCartSummary_WithExistingCart_ShouldReturnSummary() {
        // Given
        Long userId = 1L;
        CartResponse cartResponse = CartResponse.builder()
            .id(1L)
            .userId(userId)
            .itemCount(2)
            .subtotal(new BigDecimal("199.98"))
            .total(new BigDecimal("199.98"))
            .currency("USD")
            .build();

        when(getCartUseCase.executeIfExists(userId)).thenReturn(Optional.of(cartResponse));

        // When
        CartSummaryResponse response = cartService.getCartSummary(userId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getItemCount()).isEqualTo(2);
        assertThat(response.getSubtotal()).isEqualByComparingTo(new BigDecimal("199.98"));
    }

    @Test
    void getCartSummary_WithNoCart_ShouldReturnEmptySummary() {
        // Given
        Long userId = 1L;
        when(getCartUseCase.executeIfExists(userId)).thenReturn(Optional.empty());

        // When
        CartSummaryResponse response = cartService.getCartSummary(userId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getItemCount()).isEqualTo(0);
        assertThat(response.getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void clearCart_WithValidUser_ShouldDelegateToUseCase() {
        // Given
        Long userId = 1L;

        // When
        cartService.clearCart(userId);

        // Then
        verify(clearCartUseCase).execute(userId);
    }
}