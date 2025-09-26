package com.dentallab.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for handling JWT (JSON Web Tokens) in the application.
 * <p>
 * This class is responsible for:
 * - Generating JWT tokens
 * - Extracting claims (username, expiration, etc.)
 * - Validating tokens against user details
 */
@Component
public class JwtUtil {

    /**
     * Secret key used to sign JWTs.
     * <p>
     * NOTE: In production, this should be stored securely (e.g., environment variables,
     * vault services) and never hardcoded.
     */
    private static final String SECRET_KEY = "replace_this_with_a_very_long_secure_secret_key_123456";

    /**
     * Default token expiration time in milliseconds.
     * <p>
     * Here it is set to 15 minutes (15 * 60 * 1000 ms).
     */
    private static final long JWT_EXPIRATION_MS = 15 * 60 * 1000;

    /**
     * Returns the cryptographic signing key used for signing and verifying JWTs.
     * <p>
     * Uses HMAC-SHA256 algorithm with a Base64-encoded secret.
     *
     * @return Key object used for signing JWTs
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token with custom claims.
     *
     * @param userDetails The user details (username, password, authorities)
     * @param extraClaims Any additional claims to include in the token (roles, persona, etc.)
     * @return A signed JWT token as a String
     */
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims) // custom claims go here
                .setSubject(userDetails.getUsername()) // subject = username
                .setIssuedAt(new Date(System.currentTimeMillis())) // token issue date
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS)) // expiry
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign with key
                .compact();
    }

    /**
     * Generates a JWT token without extra claims.
     *
     * @param userDetails The user details (username, password, authorities)
     * @return A signed JWT token as a String
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token The JWT token
     * @return Username contained in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using a resolver function.
     *
     * @param token          The JWT token
     * @param claimsResolver A function to resolve the claim from the Claims object
     * @param <T>            Type of the claim to return
     * @return Extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validates a JWT token against user details.
     * <p>
     * Checks that:
     * - The username in the token matches the given UserDetails
     * - The token has not expired
     *
     * @param token       The JWT token
     * @param userDetails The user details to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token The JWT token
     * @return true if expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token The JWT token
     * @return Expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Parses the JWT token and extracts all claims.
     *
     * @param token The JWT token
     * @return Claims object containing all claims from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // verify with signing key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
