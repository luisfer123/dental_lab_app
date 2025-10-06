package com.dentallab.model;

import java.util.List;

public class Dentist extends AbstractRoleProfile {
	
    private Long id;
    private String name;
    private String clinicName;
    private String licenseNumber;
    private String specialty;
    private List<DentistPhone> phones;
    private List<DentistEmail> emails;
    private List<DentistAddress> addresses;

    // Constructors --------------------
    public Dentist() {}

    public Dentist(Long id, String name, String clinicName) {
        this.id = id;
        this.name = name;
        this.clinicName = clinicName;
    }

    // Getters and setters --------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public List<DentistPhone> getPhones() { return phones; }
    public void setPhones(List<DentistPhone> phones) { this.phones = phones; }

    public List<DentistEmail> getEmails() { return emails; }
    public void setEmails(List<DentistEmail> emails) { this.emails = emails; }

    public List<DentistAddress> getAddresses() { return addresses; }
    public void setAddresses(List<DentistAddress> addresses) { this.addresses = addresses; }
}