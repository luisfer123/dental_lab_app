package com.dentallab.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long JWT_EXPIRATION_MS;

    @Value("${jwt.log-tokens:false}") // default = false
    private boolean logTokens;

    private Key getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            log.debug("Signing key successfully decoded from Base64 ({} bytes)", keyBytes.length);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Failed to decode JWT secret. Ensure it's a valid Base64 string.", e);
            throw e;
        }
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.info("Generated JWT for user [{}] with expiration at {}", 
                 userDetails.getUsername(), 
                 new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS));

        if (logTokens && log.isDebugEnabled()) {
            log.debug("[DEV ONLY] Generated raw token: {}", token);
        }

        return token;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        log.debug("Extracted username [{}] from token", username);

        if (logTokens && log.isDebugEnabled()) {
            log.debug("[DEV ONLY] Raw token being parsed: {}", token);
        }

        return username;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean valid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

        if (valid) {
            log.debug("Token is valid for user [{}]", username);
        } else {
            log.warn("Token validation failed for user [{}]", username);
            if (logTokens && log.isDebugEnabled()) {
                log.debug("[DEV ONLY] Invalid token: {}", token);
            }
        }

        return valid;
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        if (expired) {
            log.warn("Token has expired at {}", extractExpiration(token));
            if (logTokens && log.isDebugEnabled()) {
                log.debug("[DEV ONLY] Expired token: {}", token);
            }
        }
        return expired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.debug("Successfully extracted claims (subject: {}, expiration: {})",
                      claims.getSubject(), claims.getExpiration());

            return claims;
        } catch (Exception e) {
            log.error("Failed to parse JWT token", e);
            if (logTokens && log.isDebugEnabled()) {
                log.debug("[DEV ONLY] Token that failed parsing: {}", token);
            }
            throw e;
        }
    }
}
