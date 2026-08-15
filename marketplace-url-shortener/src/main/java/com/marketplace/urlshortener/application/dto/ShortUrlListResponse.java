package com.marketplace.urlshortener.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlListResponse {

    private List<ShortUrlResponse> urls;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
}