package com.marketplace.wishlist.application.usecase;

import com.marketplace.wishlist.application.dto.AddToWishlistRequest;
import com.marketplace.wishlist.application.dto.WishlistItemResponse;
import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.entity.WishlistItem;
import com.marketplace.wishlist.domain.event.ItemAddedToWishlistEvent;
import com.marketplace.wishlist.domain.exception.DuplicateWishlistItemException;
import com.marketplace.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddToWishlistUseCaseTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private AddToWishlistUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddToWishlistUseCase(wishlistRepository, eventPublisher);
    }

    @Test
    void execute_WithValidRequest_ShouldAddItemToWishlist() {
        // Given
        Long userId = 1L;
        AddToWishlistRequest request = AddToWishlistRequest.builder()
            .productId(100L)
            .variantId(200L)
            .productName("Wireless Headphones")
            .variantName("Black")
            .unitPrice(new BigDecimal("99.99"))
            .imageUrl("https://example.com/image.jpg")
            .vendorName("Tech Store")
            .vendorId(10L)
            .build();

        when(wishlistRepository.findByUserIdAndIsDefault(userId, true)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> {
            Wishlist wishlist = invocation.getArgument(0);
            WishlistItem item = wishlist.getItems().get(0);
            setField(item, "id", 1L);
            setField(wishlist, "id", 1L);
            return wishlist;
        });

        // When
        WishlistItemResponse response = useCase.execute(userId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(100L);
        assertThat(response.getVariantId()).isEqualTo(200L);
        assertThat(response.getProductName()).isEqualTo("Wireless Headphones");
        assertThat(response.getUnitPrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(response.getVendorName()).isEqualTo("Tech Store");

        verify(wishlistRepository).save(any(Wishlist.class));
        verify(eventPublisher).publishEvent(any(ItemAddedToWishlistEvent.class));
    }

    @Test
    void execute_WithExistingWishlist_ShouldAddItemToExistingWishlist() {
        // Given
        Long userId = 1L;
        AddToWishlistRequest request = AddToWishlistRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .build();

        Wishlist existingWishlist = new Wishlist(userId);
        setField(existingWishlist, "id", 1L);

        when(wishlistRepository.findByUserIdAndIsDefault(userId, true)).thenReturn(Optional.of(existingWishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        WishlistItemResponse response = useCase.execute(userId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(100L);

        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void execute_WithDuplicateProduct_ShouldThrowDuplicateWishlistItemException() {
        // Given
        Long userId = 1L;
        AddToWishlistRequest request = AddToWishlistRequest.builder()
            .productId(100L)
            .productName("Wireless Headphones")
            .unitPrice(new BigDecimal("99.99"))
            .build();

        Wishlist existingWishlist = new Wishlist(userId);
        setField(existingWishlist, "id", 1L);
        existingWishlist.addItem(100L, null, "Wireless Headphones", null,
                                 new BigDecimal("99.99"), null, null, null);

        when(wishlistRepository.findByUserIdAndIsDefault(userId, true)).thenReturn(Optional.of(existingWishlist));

        // When & Then
        assertThatThrownBy(() -> useCase.execute(userId, request))
            .isInstanceOf(DuplicateWishlistItemException.class)
            .hasMessageContaining("100");

        verify(wishlistRepository, never()).save(any(Wishlist.class));
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