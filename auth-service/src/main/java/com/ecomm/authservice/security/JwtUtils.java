package com.ecomm.authservice.security;

import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.Cookie;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;

    @Value("${jwt.cookie-secure:true}")
    private boolean cookieSecure;

    private static final String ACCESS_COOKIE = "access_token";
    private static final String REFRESH_COOKIE = "refresh_token";

    // ─── Token yaratmaq ───────────────────────────────────────────
    public String generateAccessToken(UUID userId, String email, List<String> roles) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId.toString())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String email) {
        return buildToken(email, refreshTokenExpirationMs);
    }

    // ─── Cookie yaratmaq ───────────────────────────────────────────
    public ResponseCookie generateAccessCookie(UUID userId, String email, List<String> roles) {
        String token = generateAccessToken(userId, email, roles);
        return buildCookie(ACCESS_COOKIE, token, accessTokenExpirationMs / 1000);
    }

    public ResponseCookie generateRefreshCookie(String email) {
        String token = generateRefreshToken(email);
        return buildCookie(REFRESH_COOKIE, token, refreshTokenExpirationMs / 1000);
    }

    public ResponseCookie clearAccessCookie() {
        return buildCookie(ACCESS_COOKIE, "", 0);
    }

    public ResponseCookie clearRefreshCookie() {
        return buildCookie(REFRESH_COOKIE, "", 0);
    }

    // ─── Cookie-dən token oxumaq ────────────────────────────────────
    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, ACCESS_COOKIE);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, REFRESH_COOKIE);
    }

    // ─── Token-dən məlumat çıxarmaq ─────────────────────────────────
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT müddəti bitib: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT dəstəklənmir: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT deformasiyaya uğrayıb: {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("JWT xətası: {}", e.getMessage());
        }
        return false;
    }


    // ─── Private köməkçi metodlar ────────────────────────────────────
    private String buildToken(String subject, long expirationMs) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    private ResponseCookie buildCookie(String name, String value, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite("Strict")
                .maxAge(maxAgeSeconds)
                .build();
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}