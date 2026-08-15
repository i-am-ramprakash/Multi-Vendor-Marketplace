package com.marketplace.urlshortener.application.usecase;

import com.marketplace.urlshortener.application.dto.ClickAnalyticsResponse;
import com.marketplace.urlshortener.domain.entity.UrlClick;
import com.marketplace.urlshortener.domain.repository.UrlClickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetClickAnalyticsUseCase {

    private final UrlClickRepository clickRepository;

    @Transactional(readOnly = true)
    public ClickAnalyticsResponse execute(String shortCode, int days) {
        Instant start = LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = Instant.now();

        List<UrlClick> clicks = clickRepository.findByShortCode(shortCode);

        // Filter clicks within the date range
        List<UrlClick> filteredClicks = clicks.stream()
            .filter(click -> click.getClickedAt().isAfter(start) && click.getClickedAt().isBefore(end))
            .collect(Collectors.toList());

        // Calculate daily clicks
        List<ClickAnalyticsResponse.DailyClicks> dailyClicks = calculateDailyClicks(filteredClicks, days);

        // Calculate device clicks
        List<ClickAnalyticsResponse.DeviceClicks> deviceClicks = calculateDeviceClicks(filteredClicks);

        // Calculate browser clicks
        List<ClickAnalyticsResponse.BrowserClicks> browserClicks = calculateBrowserClicks(filteredClicks);

        // Calculate country clicks
        List<ClickAnalyticsResponse.CountryClicks> countryClicks = calculateCountryClicks(filteredClicks);

        // Calculate referer clicks
        List<ClickAnalyticsResponse.RefererClicks> refererClicks = calculateRefererClicks(filteredClicks);

        return ClickAnalyticsResponse.builder()
            .shortCode(shortCode)
            .totalClicks(filteredClicks.size())
            .dailyClicks(dailyClicks)
            .deviceClicks(deviceClicks)
            .browserClicks(browserClicks)
            .countryClicks(countryClicks)
            .refererClicks(refererClicks)
            .build();
    }

    private List<ClickAnalyticsResponse.DailyClicks> calculateDailyClicks(List<UrlClick> clicks, int days) {
        Map<String, Long> dailyClicksMap = clicks.stream()
            .collect(Collectors.groupingBy(
                click -> click.getClickedAt().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                Collectors.counting()
            ));

        List<ClickAnalyticsResponse.DailyClicks> dailyClicks = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            String date = LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            long count = dailyClicksMap.getOrDefault(date, 0L);
            dailyClicks.add(ClickAnalyticsResponse.DailyClicks.builder()
                .date(date)
                .clicks(count)
                .uniqueClicks(count) // Simplified
                .build());
        }

        return dailyClicks;
    }

    private List<ClickAnalyticsResponse.DeviceClicks> calculateDeviceClicks(List<UrlClick> clicks) {
        Map<String, Long> deviceCounts = clicks.stream()
            .filter(click -> click.getDevice() != null)
            .collect(Collectors.groupingBy(UrlClick::getDevice, Collectors.counting()));

        long total = clicks.size();
        return deviceCounts.entrySet().stream()
            .map(entry -> ClickAnalyticsResponse.DeviceClicks.builder()
                .device(entry.getKey())
                .count(entry.getValue())
                .percentage(total > 0 ? (double) entry.getValue() / total * 100 : 0)
                .build())
            .sorted(Comparator.comparing(ClickAnalyticsResponse.DeviceClicks::getCount).reversed())
            .collect(Collectors.toList());
    }

    private List<ClickAnalyticsResponse.BrowserClicks> calculateBrowserClicks(List<UrlClick> clicks) {
        Map<String, Long> browserCounts = clicks.stream()
            .filter(click -> click.getBrowser() != null)
            .collect(Collectors.groupingBy(UrlClick::getBrowser, Collectors.counting()));

        long total = clicks.size();
        return browserCounts.entrySet().stream()
            .map(entry -> ClickAnalyticsResponse.BrowserClicks.builder()
                .browser(entry.getKey())
                .count(entry.getValue())
                .percentage(total > 0 ? (double) entry.getValue() / total * 100 : 0)
                .build())
            .sorted(Comparator.comparing(ClickAnalyticsResponse.BrowserClicks::getCount).reversed())
            .collect(Collectors.toList());
    }

    private List<ClickAnalyticsResponse.CountryClicks> calculateCountryClicks(List<UrlClick> clicks) {
        Map<String, Long> countryCounts = clicks.stream()
            .filter(click -> click.getCountry() != null)
            .collect(Collectors.groupingBy(UrlClick::getCountry, Collectors.counting()));

        long total = clicks.size();
        return countryCounts.entrySet().stream()
            .map(entry -> ClickAnalyticsResponse.CountryClicks.builder()
                .country(entry.getKey())
                .count(entry.getValue())
                .percentage(total > 0 ? (double) entry.getValue() / total * 100 : 0)
                .build())
            .sorted(Comparator.comparing(ClickAnalyticsResponse.CountryClicks::getCount).reversed())
            .collect(Collectors.toList());
    }

    private List<ClickAnalyticsResponse.RefererClicks> calculateRefererClicks(List<UrlClick> clicks) {
        Map<String, Long> refererCounts = clicks.stream()
            .filter(click -> click.getReferer() != null)
            .collect(Collectors.groupingBy(UrlClick::getReferer, Collectors.counting()));

        long total = clicks.size();
        return refererCounts.entrySet().stream()
            .map(entry -> ClickAnalyticsResponse.RefererClicks.builder()
                .referer(entry.getKey())
                .count(entry.getValue())
                .percentage(total > 0 ? (double) entry.getValue() / total * 100 : 0)
                .build())
            .sorted(Comparator.comparing(ClickAnalyticsResponse.RefererClicks::getCount).reversed())
            .collect(Collectors.toList());
    }
}