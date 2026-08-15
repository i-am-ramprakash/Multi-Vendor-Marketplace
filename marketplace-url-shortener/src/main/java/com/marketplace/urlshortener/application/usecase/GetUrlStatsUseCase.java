package com.marketplace.urlshortener.application.usecase;

import com.marketplace.urlshortener.application.dto.UrlStatsResponse;
import com.marketplace.urlshortener.domain.repository.ShortUrlRepository;
import com.marketplace.urlshortener.domain.repository.UrlClickRepository;
import com.marketplace.urlshortener.domain.valueobject.UrlStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class GetUrlStatsUseCase {

    private final ShortUrlRepository shortUrlRepository;
    private final UrlClickRepository clickRepository;

    @Transactional(readOnly = true)
    public UrlStatsResponse execute() {
        Instant now = Instant.now();
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY)
            .atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant startOfMonth = LocalDate.now().withDayOfMonth(1)
            .atStartOfDay(ZoneId.systemDefault()).toInstant();

        long totalUrls = shortUrlRepository.countByStatus(UrlStatus.ACTIVE) +
            shortUrlRepository.countByStatus(UrlStatus.INACTIVE) +
            shortUrlRepository.countByStatus(UrlStatus.EXPIRED);

        long activeUrls = shortUrlRepository.countByStatus(UrlStatus.ACTIVE);
        long expiredUrls = shortUrlRepository.countByStatus(UrlStatus.EXPIRED);

        return UrlStatsResponse.builder()
            .totalUrls(totalUrls)
            .activeUrls(activeUrls)
            .expiredUrls(expiredUrls)
            .build();
    }
}