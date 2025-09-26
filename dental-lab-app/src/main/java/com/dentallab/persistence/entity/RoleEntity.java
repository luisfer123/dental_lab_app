package com.dentallab.persistence.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "role",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_role_name", columnNames = {"name"})
       })
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // aligns with SERIAL/IDENTITY PKs
    @Column(name = "id") // BIGINT/INT in your DB
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<UserRoleEntity> userRoles = new HashSet<>();
    
    // Optional: add a short machine-friendly code if your schema has it
    // @Column(name = "code", nullable = false, length = 50, unique = true)
    // private String code;

    protected RoleEntity() {
        // JPA
    }

    public RoleEntity(String name) {
        this.name = name;
    }
    
    public void addUser(UserAccountEntity user) {
        UserRoleEntity userRole = new UserRoleEntity(user.getId(), this.getId());
        userRole.setUser(user);
        userRole.setRole(this);

        user.getUserRoles().add(userRole);
        this.userRoles.add(userRole);
    }

    public void removeUser(UserAccountEntity user) {
        user.getUserRoles().removeIf(ur -> ur.getRole().equals(this));
        this.userRoles.removeIf(ur -> ur.getUser().equals(user));
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    public Set<UserRoleEntity> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRoleEntity> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntity that)) return false;
        // Entities are equal if they share the same DB identity
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // Stable hash for persisted entities
        return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "RoleEntity{id=" + id + ", name='" + name + "'}";
    }
}