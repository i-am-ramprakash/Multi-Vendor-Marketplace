package com.marketplace.urlshortener.api.controller;

import com.marketplace.urlshortener.application.dto.*;
import com.marketplace.urlshortener.application.service.UrlShortenerService;
import com.marketplace.urlshortener.domain.service.RateLimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/urls")
@RequiredArgsConstructor
@Tag(name = "URL Shortener Management", description = "URL shortener management endpoints")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;
    private final RateLimitService rateLimitService;

    @PostMapping
    @Operation(summary = "Create short URL", description = "Creates a new short URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Short URL created successfully", content = @Content(schema = @Schema(implementation = ShortUrlResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error or invalid URL", content = @Content(schema = @Schema(implementation = com.marketplace.urlshortener.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Short code already exists", content = @Content(schema = @Schema(implementation = com.marketplace.urlshortener.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
    public ResponseEntity<ShortUrlResponse> createShortUrl(
            @Valid @RequestBody CreateShortUrlRequest request,
            HttpServletRequest httpRequest) {
        // Rate limiting
        String clientIp = getClientIp(httpRequest);
        if (!rateLimitService.isAllowed(clientIp, "CREATE_URL", 10, 60)) {
            throw new com.marketplace.urlshortener.domain.exception.RateLimitExceededException(
                "Rate limit exceeded for URL creation. Max 10 requests per hour."
            );
        }
        rateLimitService.recordRequest(clientIp, "CREATE_URL");

        ShortUrlResponse response = urlShortenerService.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get short URL", description = "Returns short URL details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Short URL retrieved successfully", content = @Content(schema = @Schema(implementation = ShortUrlResponse.class))),
        @ApiResponse(responseCode = "404", description = "Short URL not found", content = @Content(schema = @Schema(implementation = com.marketplace.urlshortener.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
    public ResponseEntity<ShortUrlResponse> getShortUrl(@PathVariable Long id) {
        ShortUrlResponse response = urlShortenerService.getShortUrl(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{shortCode}")
    @Operation(summary = "Get short URL by code", description = "Returns short URL details by short code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Short URL retrieved successfully", content = @Content(schema = @Schema(implementation = ShortUrlResponse.class))),
        @ApiResponse(responseCode = "404", description = "Short URL not found", content = @Content(schema = @Schema(implementation = com.marketplace.urlshortener.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
    public ResponseEntity<ShortUrlResponse> getShortUrlByCode(@PathVariable String shortCode) {
        ShortUrlResponse response = urlShortenerService.getShortUrlByCode(shortCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user short URLs", description = "Returns paginated list of user's short URLs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Short URLs retrieved successfully", content = @Content(schema = @Schema(implementation = ShortUrlListResponse.class)))
    })
    @PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
    public ResponseEntity<ShortUrlListResponse> getUserShortUrls(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ShortUrlListResponse response = urlShortenerService.getUserShortUrls(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/analytics")
    @Operation(summary = "Get URL analytics", description = "Returns analytics for a short URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully", content = @Content(schema = @Schema(implementation = UrlAnalyticsResponse.class))),
        @ApiResponse(responseCode = "404", description = "Short URL not found", content = @Content(schema = @Schema(implementation = com.marketplace.urlshortener.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
    public ResponseEntity<UrlAnalyticsResponse> getUrlAnalytics(@PathVariable Long id) {
        UrlAnalyticsResponse response = urlShortenerService.getUrlAnalytics(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{shortCode}/analytics")
    @Operation(summary = "Get URL analytics by code", description = "Returns analytics for a short URL by code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully", content = @Content(schema = @Schema(implementation = UrlAnalyticsResponse.class))),
        @ApiResponse(responseCode = "404", description = "Short URL not found", content = @Content(schema = @Schema(implementation = com.marketplace.urlshortener.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
    public ResponseEntity<UrlAnalyticsResponse> getUrlAnalyticsByCode(@PathVariable String shortCode) {
        UrlAnalyticsResponse response = urlShortenerService.getUrlAnalyticsByCode(shortCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{shortCode}/click-analytics")
    @Operation(summary = "Get click analytics", description = "Returns detailed click analytics for a short URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Click analytics retrieved successfully", content = @Content(schema = @Schema(implementation = ClickAnalyticsResponse.class))),
        @ApiResponse(responseCode = "404", description = "Short URL not found", content = @Content(schema = @Schema(implementation = com.marketplace.urlshortener.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
    public ResponseEntity<ClickAnalyticsResponse> getClickAnalytics(
            @PathVariable String shortCode,
            @RequestParam(defaultValue = "30") int days) {
        ClickAnalyticsResponse response = urlShortenerService.getClickAnalytics(shortCode, days);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate short URL", description = "Deactivates a short URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Short URL deactivated successfully", content = @Content(schema = @Schema(implementation = ShortUrlResponse.class))),
        @ApiResponse(responseCode = "404", description = "Short URL not found", content = @Content(schema = @Schema(implementation = com.marketplace.urlshortener.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
    public ResponseEntity<ShortUrlResponse> deactivateShortUrl(
            @PathVariable Long id,
            @RequestParam Long performedBy) {
        ShortUrlResponse response = urlShortenerService.deactivateShortUrl(id, performedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get URL stats", description = "Returns URL shortener statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stats retrieved successfully", content = @Content(schema = @Schema(implementation = UrlStatsResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UrlStatsResponse> getUrlStats() {
        UrlStatsResponse response = urlShortenerService.getUrlStats();
        return ResponseEntity.ok(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}