package com.ecomm.productservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils {

    private static final String ACCESS_COOKIE = "access_token";

    @Value("${jwt.secret}")
    private String jwtSecret;

    public UUID getUserIdFromToken(String token) {
        String userId = parseClaims(token)
                .get("userId", String.class);

        return UUID.fromString(userId);
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return parseClaims(token).get("roles", List.class);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException exception) {
            log.warn("JWT müddəti bitib: {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("JWT dəstəklənmir: {}", exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("JWT deformasiyaya uğrayıb: {}", exception.getMessage());
        } catch (JwtException | IllegalArgumentException exception) {
            log.warn("JWT xətası: {}", exception.getMessage());
        }

        return false;
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, ACCESS_COOKIE);

        return cookie != null
                ? cookie.getValue()
                : null;
    }


    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
}
