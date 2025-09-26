package com.dentallab.security;

import com.dentallab.persistence.entity.UserAccountEntity;
import com.dentallab.persistence.entity.UserRoleEntity;
import com.dentallab.persistence.entity.RoleEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom implementation of Spring Security's {@link UserDetails}.
 * <p>
 * This class adapts a {@link UserAccountEntity} to Spring Security's
 * authentication model. It exposes the user's username, password,
 * account status, and roles as {@link GrantedAuthority} objects.
 */
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
	private final UserAccountEntity user;

    /**
     * Constructs a new CustomUserDetails wrapper for a UserAccountEntity.
     *
     * @param user the {@link UserAccountEntity} being wrapped
     */
    public CustomUserDetails(UserAccountEntity user) {
        this.user = user;
    }

    /**
     * Returns the authorities (roles) granted to the user.
     * <p>
     * This method converts the user's {@link RoleEntity} list into
     * Spring Security {@link SimpleGrantedAuthority} objects, each
     * prefixed with "ROLE_".
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<UserRoleEntity> userRoles = user.getUserRoles(); // requires a roles relationship in UserAccountEntity
        return userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Returns the hashed password used to authenticate the user.
     *
     * @return the password hash
     */
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Indicates whether the user's account has expired.
     * <p>
     * Always returns true since account expiration is not implemented.
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return true if not locked, false if locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * <p>
     * Always returns true since credential expiration is not implemented.
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return true if enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * Returns the internal database ID of the user.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return user.getId();
    }

    /**
     * Returns the underlying {@link UserAccountEntity}.
     * <p>
     * Useful if you need direct access to entity fields not exposed by UserDetails.
     *
     * @return the wrapped user entity
     */
    public UserAccountEntity getUser() {
        return user;
    }
}
