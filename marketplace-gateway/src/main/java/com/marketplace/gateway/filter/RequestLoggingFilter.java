package com.marketplace.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String rawRequestId = request.getHeaders().getFirst(REQUEST_ID_HEADER);
        final String requestId = (rawRequestId == null || rawRequestId.isEmpty())
            ? UUID.randomUUID().toString()
            : rawRequestId;

        final String path = request.getURI().getPath();
        final String method = request.getMethod().name();
        String clientIp = getClientIp(request);
        final long startTime = System.currentTimeMillis();

        log.info("[{}] {} {} from IP: {}", requestId, method, path, clientIp);

        exchange.getResponse().getHeaders().add(REQUEST_ID_HEADER, requestId);

        ServerHttpRequest mutatedRequest = request.mutate()
            .header(REQUEST_ID_HEADER, requestId)
            .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build())
            .then(Mono.fromRunnable(() -> {
                long duration = System.currentTimeMillis() - startTime;
                int statusCode = exchange.getResponse().getStatusCode() != null
                    ? exchange.getResponse().getStatusCode().value()
                    : 0;
                log.info("[{}] {} {} - {} ({}ms)", requestId, method, path, statusCode, duration);
            }));
    }

    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        if (request.getRemoteAddress() != null) {
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
        return "unknown";
    }

    @Override
    public int getOrder() {
        return -300;
    }
}
