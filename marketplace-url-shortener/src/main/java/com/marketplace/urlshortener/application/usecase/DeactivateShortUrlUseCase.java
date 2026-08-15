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
public class DeactivateShortUrlUseCase {

    private final ShortUrlRepository shortUrlRepository;

    @Value("${app.url.shortener.base-url:http://localhost:8088/s}")
    private String baseUrl;

    @Transactional
    public ShortUrlResponse execute(Long id, Long performedBy) {
        ShortUrl shortUrl = shortUrlRepository.findById(id)
            .orElseThrow(() -> new UrlNotFoundException(id));

        shortUrl.deactivate();
        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);

        return ShortUrlResponse.from(savedShortUrl, baseUrl);
    }

    @Transactional
    public ShortUrlResponse executeByShortCode(String shortCode, Long performedBy) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new UrlNotFoundException(shortCode));

        shortUrl.deactivate();
        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);

        return ShortUrlResponse.from(savedShortUrl, baseUrl);
    }
}