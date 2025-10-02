package com.dentallab.representationmodel;

import org.springframework.hateoas.RepresentationModel;

public class DentistAddressRepresentation extends RepresentationModel<DentistAddressRepresentation> {
    private Long id;
    private String address;
    private String type;       // MAIN, BRANCH, BILLING
    private boolean primary;
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
