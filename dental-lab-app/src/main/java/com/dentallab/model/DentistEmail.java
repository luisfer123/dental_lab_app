package com.dentallab.model;

public class DentistEmail {
    private Long id;
    private Long dentistId;
    private String email;
    private String type;     // WORK / PERSONAL
    private boolean primary;
    private boolean active;

    public DentistEmail() {}

    public DentistEmail(Long id, Long dentistId, String email, String type, boolean primary, boolean active) {
        this.id = id;
        this.dentistId = dentistId;
        this.email = email;
        this.type = type;
        this.primary = primary;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDentistId() { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

