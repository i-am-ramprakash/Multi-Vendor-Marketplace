package com.marketplace.admin.config;

import java.io.IOException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${app.jwt.secret:your-super-secret-jwt-key-that-is-at-least-256-bits-long-for-hs256}")
    private String jwtSecret;

    @Value("${app.jwt.issuer:multivendor-marketplace}")
    private String expectedIssuer;

    @Value("${app.jwt.audience:multivendor-marketplace-api}")
    private String expectedAudience;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .requireIssuer(expectedIssuer)
                        .requireAudience(expectedAudience)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String tokenType = claims.get("tokenType", String.class);
                if (!"ACCESS".equals(tokenType)) {
                    log.debug("Non-access token used for admin request");
                    filterChain.doFilter(request, response);
                    return;
                }

                @SuppressWarnings("unchecked")
                Set<String> roles = claims.get("roles", Set.class);
                if (roles == null) {
                    roles = Set.of();
                }

                boolean isAdmin = roles.stream()
                        .anyMatch(r -> r.equals("ROLE_ADMIN") || r.equals("ADMIN"));

                if (!isAdmin) {
                    log.debug("Non-admin user attempted admin access: {}", claims.getSubject());
                    filterChain.doFilter(request, response);
                    return;
                }

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                log.debug("JWT token expired: {}", e.getMessage());
            } catch (JwtException e) {
                log.debug("Invalid JWT token: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}