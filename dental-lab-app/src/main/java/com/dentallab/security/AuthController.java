package com.dentallab.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentallab.persistence.entity.RefreshTokenEntity;

/**
 * REST controller for handling authentication requests.
 * <p>
 * Provides login and token refresh endpoints:
 * - /auth/login   : authenticates user and returns a JWT
 * - /auth/refresh : generates a new JWT from a refresh token
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Handles login requests.
     * <p>
     * Validates the provided username and password, and if valid,
     * generates a JWT access token and (optionally) a refresh token.
     *
     * @param request login request containing username and password
     * @return JWT token(s) in response body
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        System.out.println("Authentication successful for user: " + request.getUsername());

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", userDetails.getUserId());
        extraClaims.put("roles", userDetails.getAuthorities());

        String accessToken = jwtUtil.generateToken(userDetails, extraClaims);

        // create and save refresh token
        RefreshTokenEntity refreshToken = refreshTokenService.createToken(userDetails.getUser());

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken.getToken());

        return ResponseEntity.ok(response);
    }

    /**
     * Handles refresh token requests.
     * <p>
     * For now, this method assumes the refresh token is just another JWT
     * (in practice, you'd store/validate refresh tokens in a DB).
     *
     * @param request refresh request containing a refresh token
     * @return new JWT access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
    	return refreshTokenService.validateToken(request.getRefreshToken())
    	        .map(refreshToken -> {
    	            UserDetails userDetails = userDetailsService.loadUserByUsername(
    	                    refreshToken.getUser().getUsername()
    	            );
    	            String newAccessToken = jwtUtil.generateToken(userDetails);

    	            Map<String, Object> response = new HashMap<>();
    	            response.put("accessToken", newAccessToken);
    	            return ResponseEntity.ok(response);
    	        })
    	        .orElseGet(() -> {
    	            Map<String, Object> error = new HashMap<>();
    	            error.put("error", "Invalid or expired refresh token");
    	            return ResponseEntity.badRequest().body(error);
    	        });
    }
    
    /**
	 * Handles logout requests by revoking the provided refresh token.
	 *
	 * @param request logout request containing a refresh token
	 * @return confirmation message
	 */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest request) {
        refreshTokenService.revokeToken(request.getRefreshToken());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User logged out. Refresh token revoked.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * DTO for login request payload.
     */
    public static class LoginRequest {
        private String username;
        private String password;

        // getters and setters
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * DTO for refresh request payload.
     */
    public static class RefreshRequest {
        private String refreshToken;

        // getters and setters
        public String getRefreshToken() {
            return refreshToken;
        }
        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
