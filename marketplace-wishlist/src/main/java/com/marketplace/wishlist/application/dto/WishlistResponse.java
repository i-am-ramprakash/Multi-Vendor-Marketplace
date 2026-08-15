package com.marketplace.wishlist.application.dto;

import com.marketplace.wishlist.domain.entity.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {

    private Long id;
    private Long userId;
    private String name;
    private boolean isDefault;
    private int itemCount;
    private List<WishlistItemResponse> items;
    private Instant createdAt;
    private Instant updatedAt;

    public static WishlistResponse from(Wishlist wishlist) {
        return WishlistResponse.builder()
            .id(wishlist.getId())
            .userId(wishlist.getUserId())
            .name(wishlist.getName())
            .isDefault(wishlist.isDefault())
            .itemCount(wishlist.getItemCount())
            .items(wishlist.getItems().stream()
                .map(WishlistItemResponse::from)
                .collect(Collectors.toList()))
            .createdAt(wishlist.getCreatedAt())
            .updatedAt(wishlist.getUpdatedAt())
            .build();
    }
}