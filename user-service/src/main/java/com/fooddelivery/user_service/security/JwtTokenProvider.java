package com.fooddelivery.user_service.security;

import com.fooddelivery.user_service.model.User;
import io.jsonwebtoken.*;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationInMs;

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        String userEmail = userPrincipal.getEmail();

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userEmail)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Неправильная JWT подпись: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Некорректный JWT токен: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Срок действия JWT токена истек: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Неподдерживаемый JWT токен: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("Строка JWT claims пуста: {}", ex.getMessage());
        }
        return false;
    }
}