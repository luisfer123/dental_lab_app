package com.dentallab.representationmodel;

import org.springframework.hateoas.RepresentationModel;

public class TechnicianPhoneRepresentation extends RepresentationModel<TechnicianPhoneRepresentation> {
    private Long id;
    private String phone;
    private String type;
    private boolean primary;
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
