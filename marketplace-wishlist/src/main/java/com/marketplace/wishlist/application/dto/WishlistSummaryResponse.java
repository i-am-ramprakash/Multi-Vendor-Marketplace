package com.marketplace.wishlist.application.dto;

import com.marketplace.wishlist.domain.entity.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistSummaryResponse {

    private Long id;
    private int itemCount;
    private Instant updatedAt;

    public static WishlistSummaryResponse from(Wishlist wishlist) {
        return WishlistSummaryResponse.builder()
            .id(wishlist.getId())
            .itemCount(wishlist.getItemCount())
            .updatedAt(wishlist.getUpdatedAt())
            .build();
    }

    public static WishlistSummaryResponse from(WishlistResponse wishlist) {
        return WishlistSummaryResponse.builder()
            .id(wishlist.getId())
            .itemCount(wishlist.getItemCount())
            .updatedAt(wishlist.getUpdatedAt())
            .build();
    }
}