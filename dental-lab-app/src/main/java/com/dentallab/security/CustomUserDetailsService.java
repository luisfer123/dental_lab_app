package com.dentallab.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dentallab.persistence.entity.UserAccountEntity;
import com.dentallab.persistence.repository.UserAccountRepository;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService}.
 * <p>
 * This service is responsible for loading user data from the database
 * when authentication is requested. It queries the {@link UserAccountRepository}
 * to fetch a {@link UserAccountEntity} by username and adapts it into
 * a {@link CustomUserDetails} object for use by Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    /**
     * Constructor-based dependency injection for the repository.
     *
     * @param userAccountRepository repository for accessing user accounts
     */
    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * Loads a user from the database given its username.
     * <p>
     * This method is automatically called by Spring Security during
     * the authentication process. If the user is found, it is wrapped
     * in a {@link CustomUserDetails} instance.
     *
     * @param username the username identifying the user
     * @return a fully populated {@link UserDetails} object
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccountEntity user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(user);
    }
}

