package com.dentallab.model;

public class TechnicianEmail {
    private Long id;
    private Long technicianId;
    private String email;
    private String type;
    private boolean primary;
    private boolean active;

    public TechnicianEmail() {}

    public TechnicianEmail(Long id, Long technicianId, String email, String type, boolean primary, boolean active) {
        this.id = id;
        this.technicianId = technicianId;
        this.email = email;
        this.type = type;
        this.primary = primary;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
