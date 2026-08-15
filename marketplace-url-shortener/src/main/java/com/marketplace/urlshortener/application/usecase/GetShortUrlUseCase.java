package com.marketplace.urlshortener.application.usecase;

import com.marketplace.urlshortener.application.dto.ShortUrlResponse;
import com.marketplace.urlshortener.domain.entity.ShortUrl;
import com.marketplace.urlshortener.domain.exception.UrlNotFoundException;
import com.marketplace.urlshortener.domain.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetShortUrlUseCase {

    private final ShortUrlRepository shortUrlRepository;

    @Value("${app.url.shortener.base-url:http://localhost:8088/s}")
    private String baseUrl;

    @Transactional(readOnly = true)
    public ShortUrlResponse execute(Long id) {
        ShortUrl shortUrl = shortUrlRepository.findById(id)
            .orElseThrow(() -> new UrlNotFoundException(id));
        return ShortUrlResponse.from(shortUrl, baseUrl);
    }

    @Transactional(readOnly = true)
    public ShortUrlResponse executeByShortCode(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new UrlNotFoundException(shortCode));
        return ShortUrlResponse.from(shortUrl, baseUrl);
    }
}