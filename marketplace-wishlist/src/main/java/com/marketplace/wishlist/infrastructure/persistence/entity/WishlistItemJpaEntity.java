package com.marketplace.wishlist.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "wishlist_items", indexes = {
    @Index(name = "idx_wishlist_items_wishlist_id", columnList = "wishlist_id"),
    @Index(name = "idx_wishlist_items_product_id", columnList = "product_id"),
    @Index(name = "idx_wishlist_items_variant_id", columnList = "variant_id"),
    @Index(name = "idx_wishlist_items_product_variant", columnList = "product_id, variant_id")
})
@Data
@NoArgsConstructor
public class WishlistItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private WishlistJpaEntity wishlist;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "variant_name", length = 255)
    private String variantName;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "vendor_name", length = 255)
    private String vendorName;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private Instant addedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}