package com.marketplace.urlshortener.application.service;

import com.marketplace.urlshortener.application.dto.*;
import com.marketplace.urlshortener.application.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceImplTest {

    @Mock
    private CreateShortUrlUseCase createShortUrlUseCase;

    @Mock
    private ResolveUrlUseCase resolveUrlUseCase;

    @Mock
    private GetShortUrlUseCase getShortUrlUseCase;

    @Mock
    private GetUserShortUrlsUseCase getUserShortUrlsUseCase;

    @Mock
    private GetUrlAnalyticsUseCase getUrlAnalyticsUseCase;

    @Mock
    private GetClickAnalyticsUseCase getClickAnalyticsUseCase;

    @Mock
    private DeactivateShortUrlUseCase deactivateShortUrlUseCase;

    @Mock
    private GetUrlStatsUseCase getUrlStatsUseCase;

    private UrlShortenerServiceImpl urlShortenerService;

    @BeforeEach
    void setUp() {
        urlShortenerService = new UrlShortenerServiceImpl(
            createShortUrlUseCase,
            resolveUrlUseCase,
            getShortUrlUseCase,
            getUserShortUrlsUseCase,
            getUrlAnalyticsUseCase,
            getClickAnalyticsUseCase,
            deactivateShortUrlUseCase,
            getUrlStatsUseCase
        );
    }

    @Test
    void createShortUrl_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        CreateShortUrlRequest request = CreateShortUrlRequest.builder()
            .originalUrl("https://example.com/product/123")
            .type("PRODUCT")
            .createdBy(1L)
            .build();

        ShortUrlResponse expectedResponse = ShortUrlResponse.builder()
            .id(1L)
            .shortCode("abc123")
            .originalUrl("https://example.com/product/123")
            .type("PRODUCT")
            .status("ACTIVE")
            .build();

        when(createShortUrlUseCase.execute(request)).thenReturn(expectedResponse);

        // When
        ShortUrlResponse response = urlShortenerService.createShortUrl(request);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(createShortUrlUseCase).execute(request);
    }

    @Test
    void resolveUrl_WithValidCode_ShouldDelegateToUseCase() {
        // Given
        String expectedUrl = "https://example.com/product/123";
        when(resolveUrlUseCase.execute("abc123", "127.0.0.1", "Mozilla/5.0", null, null))
            .thenReturn(expectedUrl);

        // When
        String result = urlShortenerService.resolveUrl("abc123", "127.0.0.1", "Mozilla/5.0", null, null);

        // Then
        assertThat(result).isEqualTo(expectedUrl);
        verify(resolveUrlUseCase).execute("abc123", "127.0.0.1", "Mozilla/5.0", null, null);
    }

    @Test
    void getShortUrl_WithValidId_ShouldDelegateToUseCase() {
        // Given
        ShortUrlResponse expectedResponse = ShortUrlResponse.builder()
            .id(1L)
            .shortCode("abc123")
            .originalUrl("https://example.com/product/123")
            .build();

        when(getShortUrlUseCase.execute(1L)).thenReturn(expectedResponse);

        // When
        ShortUrlResponse response = urlShortenerService.getShortUrl(1L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getShortUrlUseCase).execute(1L);
    }

    @Test
    void getShortUrlByCode_WithValidCode_ShouldDelegateToUseCase() {
        // Given
        ShortUrlResponse expectedResponse = ShortUrlResponse.builder()
            .id(1L)
            .shortCode("abc123")
            .originalUrl("https://example.com/product/123")
            .build();

        when(getShortUrlUseCase.executeByShortCode("abc123")).thenReturn(expectedResponse);

        // When
        ShortUrlResponse response = urlShortenerService.getShortUrlByCode("abc123");

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getShortUrlUseCase).executeByShortCode("abc123");
    }

    @Test
    void getUserShortUrls_WithValidUser_ShouldDelegateToUseCase() {
        // Given
        ShortUrlListResponse expectedResponse = ShortUrlListResponse.builder()
            .totalElements(1)
            .build();

        when(getUserShortUrlsUseCase.execute(1L, 0, 10)).thenReturn(expectedResponse);

        // When
        ShortUrlListResponse response = urlShortenerService.getUserShortUrls(1L, 0, 10);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getUserShortUrlsUseCase).execute(1L, 0, 10);
    }

    @Test
    void getUrlAnalytics_WithValidId_ShouldDelegateToUseCase() {
        // Given
        UrlAnalyticsResponse expectedResponse = UrlAnalyticsResponse.builder()
            .shortUrlId(1L)
            .shortCode("abc123")
            .totalClicks(100)
            .uniqueVisitors(80)
            .build();

        when(getUrlAnalyticsUseCase.execute(1L)).thenReturn(expectedResponse);

        // When
        UrlAnalyticsResponse response = urlShortenerService.getUrlAnalytics(1L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getUrlAnalyticsUseCase).execute(1L);
    }

    @Test
    void deactivateShortUrl_WithValidId_ShouldDelegateToUseCase() {
        // Given
        ShortUrlResponse expectedResponse = ShortUrlResponse.builder()
            .id(1L)
            .status("INACTIVE")
            .build();

        when(deactivateShortUrlUseCase.execute(1L, 10L)).thenReturn(expectedResponse);

        // When
        ShortUrlResponse response = urlShortenerService.deactivateShortUrl(1L, 10L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(deactivateShortUrlUseCase).execute(1L, 10L);
    }

    @Test
    void getUrlStats_ShouldDelegateToUseCase() {
        // Given
        UrlStatsResponse expectedResponse = UrlStatsResponse.builder()
            .totalUrls(100)
            .activeUrls(80)
            .expiredUrls(20)
            .build();

        when(getUrlStatsUseCase.execute()).thenReturn(expectedResponse);

        // When
        UrlStatsResponse response = urlShortenerService.getUrlStats();

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getUrlStatsUseCase).execute();
    }
}