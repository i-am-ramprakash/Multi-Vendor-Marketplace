package com.marketplace.urlshortener.api.controller;

import com.marketplace.urlshortener.application.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/s")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "URL Redirect", description = "URL redirect endpoints")
public class UrlRedirectController {

    private final UrlShortenerService urlShortenerService;

    @GetMapping("/{shortCode}")
    @Operation(summary = "Redirect to original URL", description = "Redirects to the original URL and tracks the click")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode,
            HttpServletRequest request) {

        String clientIp = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        Long userId = getUserIdFromRequest(request);

        String originalUrl = urlShortenerService.resolveUrl(
            shortCode,
            clientIp,
            userAgent,
            referer,
            userId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        headers.setPragma("no-cache");

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        try {
            String userIdHeader = request.getHeader("X-User-Id");
            if (userIdHeader != null && !userIdHeader.isEmpty()) {
                return Long.parseLong(userIdHeader);
            }
        } catch (NumberFormatException e) {
            log.debug("Invalid user ID in request header");
        }
        return null;
    }
}