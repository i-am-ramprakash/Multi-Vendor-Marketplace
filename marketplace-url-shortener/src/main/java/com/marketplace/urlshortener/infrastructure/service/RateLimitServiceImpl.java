package com.marketplace.urlshortener.infrastructure.service;

import com.marketplace.urlshortener.domain.entity.UrlRateLimit;
import com.marketplace.urlshortener.domain.repository.UrlRateLimitRepository;
import com.marketplace.urlshortener.domain.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final UrlRateLimitRepository rateLimitRepository;

    @Override
    @Transactional
    public boolean isAllowed(String identifier, String type, int maxRequests, int windowMinutes) {
        UrlRateLimit rateLimit = rateLimitRepository.findByIdentifierAndType(identifier, type)
            .orElse(null);

        if (rateLimit == null) {
            // Create new rate limit
            rateLimit = new UrlRateLimit(identifier, type, maxRequests, windowMinutes);
            rateLimitRepository.save(rateLimit);
            return true;
        }

        // Check if blocked
        if (rateLimit.isBlocked() && rateLimit.getBlockedUntil() != null) {
            if (Instant.now().isBefore(rateLimit.getBlockedUntil())) {
                log.warn("Rate limit blocked: identifier={}, type={}, blockedUntil={}",
                    identifier, type, rateLimit.getBlockedUntil());
                return false;
            } else {
                // Block expired, reset
                rateLimit.resetWindow();
                rateLimitRepository.save(rateLimit);
                return true;
            }
        }

        // Check if window expired
        if (Instant.now().isAfter(rateLimit.getWindowEnd())) {
            rateLimit.resetWindow();
            rateLimitRepository.save(rateLimit);
            return true;
        }

        // Check request count
        if (rateLimit.getRequestCount() >= rateLimit.getMaxRequests()) {
            log.warn("Rate limit exceeded: identifier={}, type={}, count={}/{}",
                identifier, type, rateLimit.getRequestCount(), rateLimit.getMaxRequests());
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public void recordRequest(String identifier, String type) {
        UrlRateLimit rateLimit = rateLimitRepository.findByIdentifierAndType(identifier, type)
            .orElse(null);

        if (rateLimit == null) {
            rateLimit = new UrlRateLimit(identifier, type, 100, 60);
        }

        rateLimit.incrementRequestCount();
        rateLimitRepository.save(rateLimit);
    }

    @Override
    @Transactional
    public void block(String identifier, String type, int blockMinutes) {
        UrlRateLimit rateLimit = rateLimitRepository.findByIdentifierAndType(identifier, type)
            .orElse(null);

        if (rateLimit == null) {
            rateLimit = new UrlRateLimit(identifier, type, 100, 60);
        }

        rateLimit.block(blockMinutes);
        rateLimitRepository.save(rateLimit);

        log.warn("Rate limit blocked: identifier={}, type={}, blockMinutes={}",
            identifier, type, blockMinutes);
    }

    @Override
    @Transactional(readOnly = true)
    public int getRequestCount(String identifier, String type) {
        return rateLimitRepository.findByIdentifierAndType(identifier, type)
            .map(UrlRateLimit::getRequestCount)
            .orElse(0);
    }
}