package com.dentallab.persistence.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserRole")
@IdClass(UserRoleEntity.UserRoleId.class)
public class UserRoleEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserAccountEntity user;
    
    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    // --- Constructors ---
    public UserRoleEntity() {}

    public UserRoleEntity(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // --- Getters & Setters ---
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public UserAccountEntity getUser() {
		return user;
	}
    
    public void setUser(UserAccountEntity user) {
    	this.user = user;
    }
    
    public RoleEntity getRole() {
    	return role;
    }
    
    public void setRole(RoleEntity role) {
		this.role = role;
	}

    // --- Composite key class ---
    public static class UserRoleId implements Serializable {
        private Long userId;
        private Long roleId;

        public UserRoleId() {}

        public UserRoleId(Long userId, Long roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserRoleId)) return false;
            UserRoleId that = (UserRoleId) o;
            return userId.equals(that.userId) && roleId.equals(that.roleId);
        }

        @Override
        public int hashCode() {
            return userId.hashCode() + roleId.hashCode();
        }
    }
}