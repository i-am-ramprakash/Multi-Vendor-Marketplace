package com.marketplace.wishlist.application.service;

import com.marketplace.wishlist.application.dto.*;
import com.marketplace.wishlist.application.usecase.*;
import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.entity.WishlistItem;
import com.marketplace.wishlist.domain.exception.WishlistNotFoundException;
import com.marketplace.wishlist.domain.repository.WishlistRepository;
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
class WishlistServiceImplTest {

    @Mock
    private AddToWishlistUseCase addToWishlistUseCase;

    @Mock
    private RemoveFromWishlistUseCase removeFromWishlistUseCase;

    @Mock
    private RemoveWishlistItemByIdUseCase removeWishlistItemByIdUseCase;

    @Mock
    private GetWishlistUseCase getWishlistUseCase;

    @Mock
    private ClearWishlistUseCase clearWishlistUseCase;

    private WishlistServiceImpl wishlistService;

    @BeforeEach
    void setUp() {
        wishlistService = new WishlistServiceImpl(
            addToWishlistUseCase,
            removeFromWishlistUseCase,
            removeWishlistItemByIdUseCase,
            getWishlistUseCase,
            clearWishlistUseCase
        );
    }

    @Test
    void addToWishlist_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        Long userId = 1L;
        AddToWishlistRequest request = AddToWishlistRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .build();

        WishlistItemResponse expectedResponse = WishlistItemResponse.builder()
            .id(1L)
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .build();

        when(addToWishlistUseCase.execute(userId, request)).thenReturn(expectedResponse);

        // When
        WishlistItemResponse response = wishlistService.addToWishlist(userId, request);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(addToWishlistUseCase).execute(userId, request);
    }

    @Test
    void removeFromWishlist_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        Long userId = 1L;
        Long productId = 100L;
        Long variantId = 200L;

        // When
        wishlistService.removeFromWishlist(userId, productId, variantId);

        // Then
        verify(removeFromWishlistUseCase).execute(userId, productId, variantId);
    }

    @Test
    void getWishlist_WithExistingWishlist_ShouldDelegateToUseCase() {
        // Given
        Long userId = 1L;
        WishlistResponse expectedResponse = WishlistResponse.builder()
            .id(1L)
            .userId(userId)
            .name("My Wishlist")
            .itemCount(2)
            .build();

        when(getWishlistUseCase.execute(userId)).thenReturn(expectedResponse);

        // When
        WishlistResponse response = wishlistService.getWishlist(userId);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getWishlistUseCase).execute(userId);
    }

    @Test
    void getWishlistSummary_WithExistingWishlist_ShouldReturnSummary() {
        // Given
        Long userId = 1L;
        WishlistResponse wishlistResponse = WishlistResponse.builder()
            .id(1L)
            .userId(userId)
            .itemCount(3)
            .build();

        when(getWishlistUseCase.executeIfExists(userId)).thenReturn(Optional.of(wishlistResponse));

        // When
        WishlistSummaryResponse response = wishlistService.getWishlistSummary(userId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getItemCount()).isEqualTo(3);
    }

    @Test
    void getWishlistSummary_WithNoWishlist_ShouldReturnEmptySummary() {
        // Given
        Long userId = 1L;
        when(getWishlistUseCase.executeIfExists(userId)).thenReturn(Optional.empty());

        // When
        WishlistSummaryResponse response = wishlistService.getWishlistSummary(userId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getItemCount()).isEqualTo(0);
    }

    @Test
    void clearWishlist_WithValidUser_ShouldDelegateToUseCase() {
        // Given
        Long userId = 1L;

        // When
        wishlistService.clearWishlist(userId);

        // Then
        verify(clearWishlistUseCase).execute(userId);
    }
}