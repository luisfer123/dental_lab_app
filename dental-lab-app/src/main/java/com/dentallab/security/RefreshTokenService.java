package com.dentallab.security;

import com.dentallab.persistence.entity.RefreshTokenEntity;
import com.dentallab.persistence.entity.UserAccountEntity;
import com.dentallab.persistence.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing refresh tokens: issue, validate, revoke.
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Creates and stores a new refresh token for the given user.
     *
     * @param user the user account
     * @return persisted refresh token
     */
    public RefreshTokenEntity createToken(UserAccountEntity user) {
        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(7)); // 7 days validity
        return refreshTokenRepository.save(token);
    }

    /**
     * Validates a refresh token.
     *
     * @param token refresh token string
     * @return optional refresh token if valid
     */
    public Optional<RefreshTokenEntity> validateToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(t -> !t.isRevoked() && t.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    /**
     * Revokes a refresh token so it cannot be used again.
     *
     * @param token refresh token string
     */
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            refreshTokenRepository.save(t);
        });
    }
}
