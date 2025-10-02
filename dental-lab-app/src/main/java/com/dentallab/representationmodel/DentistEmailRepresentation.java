package com.dentallab.representationmodel;

import org.springframework.hateoas.RepresentationModel;

public class DentistEmailRepresentation extends RepresentationModel<DentistEmailRepresentation> {
    private Long id;
    private String email;
    private String type;       // WORK, PERSONAL
    private boolean primary;
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

