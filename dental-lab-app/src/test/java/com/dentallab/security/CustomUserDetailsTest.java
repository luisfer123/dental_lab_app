package com.dentallab.security;

import com.dentallab.persistence.entity.RoleEntity;
import com.dentallab.persistence.entity.UserAccountEntity;
import com.dentallab.persistence.entity.UserRoleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void getAuthorities_shouldReturnRolesPrefixedWithROLE_() {
        // arrange
        UserAccountEntity user = new UserAccountEntity("jdoe", "secret-hash");
        user.setId(42L);

        RoleEntity admin = new RoleEntity("ADMIN");
        admin.setId(1L);

        // create user-role link
        UserRoleEntity ur = new UserRoleEntity(user.getId(), admin.getId());
        ur.setUser(user);
        ur.setRole(admin);

        // attach to user and role
        Set<UserRoleEntity> userRoles = new HashSet<>();
        userRoles.add(ur);
        user.setUserRoles(userRoles);

        Set<UserRoleEntity> roleUsers = new HashSet<>();
        roleUsers.add(ur);
        admin.setUserRoles(roleUsers);

        CustomUserDetails details = new CustomUserDetails(user);

        // act
        Collection authorities = details.getAuthorities();

        // assert
        assertThat(authorities).contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        assertThat(details.getUsername()).isEqualTo("jdoe");
        assertThat(details.getPassword()).isEqualTo("secret-hash");
    }

    @Test
    void accountLock_and_enabled_flags_are_reflected() {
        UserAccountEntity user = new UserAccountEntity("alice", "pwhash");
        user.setId(7L);

        user.setLocked(true);
        user.setEnabled(false);

        CustomUserDetails details = new CustomUserDetails(user);

        assertFalse(details.isAccountNonLocked()); // locked -> accountNonLocked == false
        assertFalse(details.isEnabled()); // enabled == false

        // flip flags
        user.setLocked(false);
        user.setEnabled(true);

        assertTrue(new CustomUserDetails(user).isAccountNonLocked());
        assertTrue(new CustomUserDetails(user).isEnabled());

        // getUser and getUserId
        CustomUserDetails d2 = new CustomUserDetails(user);
        assertEquals(7L, d2.getUserId());
        assertSame(user, d2.getUser());
    }

    @Test
    void getAuthorities_withMultipleRoles_shouldReturnAllPrefixedRoles() {
        // arrange
        UserAccountEntity user = new UserAccountEntity("bob", "hash123");
        user.setId(100L);

        RoleEntity admin = new RoleEntity("ADMIN");
        admin.setId(1L);

        RoleEntity tech = new RoleEntity("TECHNICIAN");
        tech.setId(2L);

        // create user-role links
        UserRoleEntity ur1 = new UserRoleEntity(user.getId(), admin.getId());
        ur1.setUser(user);
        ur1.setRole(admin);

        UserRoleEntity ur2 = new UserRoleEntity(user.getId(), tech.getId());
        ur2.setUser(user);
        ur2.setRole(tech);

        // attach to user and roles
        Set<UserRoleEntity> userRoles = new HashSet<>();
        userRoles.add(ur1);
        userRoles.add(ur2);
        user.setUserRoles(userRoles);

        Set<UserRoleEntity> adminUsers = new HashSet<>();
        adminUsers.add(ur1);
        admin.setUserRoles(adminUsers);

        Set<UserRoleEntity> techUsers = new HashSet<>();
        techUsers.add(ur2);
        tech.setUserRoles(techUsers);

        CustomUserDetails details = new CustomUserDetails(user);

        // act
        Collection authorities = details.getAuthorities();

        // assert
        assertThat(authorities).contains(new SimpleGrantedAuthority("ROLE_ADMIN"),
                                         new SimpleGrantedAuthority("ROLE_TECHNICIAN"));
        assertThat(authorities).hasSize(2);
    }
}