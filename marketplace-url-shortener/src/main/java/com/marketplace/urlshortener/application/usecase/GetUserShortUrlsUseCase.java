package com.marketplace.urlshortener.application.usecase;

import com.marketplace.urlshortener.application.dto.ShortUrlListResponse;
import com.marketplace.urlshortener.application.dto.ShortUrlResponse;
import com.marketplace.urlshortener.domain.entity.ShortUrl;
import com.marketplace.urlshortener.domain.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetUserShortUrlsUseCase {

    private final ShortUrlRepository shortUrlRepository;

    @Value("${app.url.shortener.base-url:http://localhost:8088/s}")
    private String baseUrl;

    @Transactional(readOnly = true)
    public ShortUrlListResponse execute(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ShortUrl> urls = shortUrlRepository.findByCreatedBy(userId, pageRequest);

        List<ShortUrlResponse> content = urls.getContent().stream()
            .map(url -> ShortUrlResponse.from(url, baseUrl))
            .collect(Collectors.toList());

        return ShortUrlListResponse.builder()
            .urls(content)
            .totalElements(urls.getTotalElements())
            .totalPages(urls.getTotalPages())
            .page(urls.getNumber())
            .size(urls.getSize())
            .build();
    }
}