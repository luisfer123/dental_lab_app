package com.dentallab.persistence.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserAccount")
public class UserAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @OneToMany
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    // --- Constructors ---
    public UserAccountEntity() {}

    public UserAccountEntity(String username, String passwordHash) {
        this.username = username;
        this.password = passwordHash;
        this.enabled = true;
        this.locked = false;
    }
    
    public void addRole(RoleEntity role) {
        UserRoleEntity userRole = new UserRoleEntity(this.getId(), role.getId());
        userRole.setUser(this);
        userRole.setRole(role);

        userRoles.add(userRole);
        role.getUserRoles().add(userRole);
    }

    public void removeRole(RoleEntity role) {
        userRoles.removeIf(ur -> ur.getRole().equals(role));
        role.getUserRoles().removeIf(ur -> ur.getUser().equals(this));
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return password;
    }

    public void setPasswordHash(String passwordHash) {
        this.password = passwordHash;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public Set<UserRoleEntity> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRoleEntity> userRoles) {
        this.userRoles = userRoles;
    }

    // createdAt should be set by DB, so we don’t add a setter
}
