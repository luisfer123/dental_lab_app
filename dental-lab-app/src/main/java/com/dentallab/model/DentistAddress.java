package com.dentallab.model;

public class DentistAddress {
    private Long id;
    private Long dentistId;
    private String address;
    private String type;     // MAIN / BRANCH / BILLING
    private boolean primary;
    private boolean active;

    public DentistAddress() {}

    public DentistAddress(Long id, Long dentistId, String address, String type, boolean primary, boolean active) {
        this.id = id;
        this.dentistId = dentistId;
        this.address = address;
        this.type = type;
        this.primary = primary;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDentistId() { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
