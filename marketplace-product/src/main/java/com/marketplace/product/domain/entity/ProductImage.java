package com.marketplace.product.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {

    private Long id;
    private Product product;
    private ProductVariant variant;
    private String url;
    private String altText;
    private Integer position;
    private Boolean isPrimary;
    private Instant createdAt;

    public ProductImage(String url, String altText, Integer position, Boolean isPrimary) {
        this.url = url;
        this.altText = altText;
        this.position = position;
        this.isPrimary = isPrimary;
    }

    public static ProductImage primary(String url, String altText) {
        return new ProductImage(url, altText, 0, true);
    }

    public static ProductImage secondary(String url, String altText, int position) {
        return new ProductImage(url, altText, position, false);
    }

    public void setAsPrimary() {
        this.isPrimary = true;
        this.position = 0;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isForVariant() {
        return this.variant != null;
    }

    public boolean isForProduct() {
        return this.variant == null;
    }
}
