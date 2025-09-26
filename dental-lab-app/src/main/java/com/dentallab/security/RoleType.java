package com.dentallab.security;

public enum RoleType {
    ADMIN,
    DENTIST,
    TECHNICIAN,
    PATIENT;

    public String asAuthority() {
        return "ROLE_" + this.name();
    }
}
