package com.dentallab.model;

import java.util.List;

public class Dentist extends AbstractRoleProfile {
	
    private String clinicName;
    private String licenseNumber;
    private String specialty;
    private List<DentistPhone> phones;
    private List<DentistEmail> emails;
    private List<DentistAddress> addresses;

    // Constructors --------------------
    public Dentist() {}

    // Getters and setters --------------
    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public List<DentistPhone> getPhones() { return phones; }
    public void setPhones(List<DentistPhone> phones) { this.phones = phones; }

    public List<DentistEmail> getEmails() { return emails; }
    public void setEmails(List<DentistEmail> emails) { this.emails = emails; }

    public List<DentistAddress> getAddresses() { return addresses; }
    public void setAddresses(List<DentistAddress> addresses) { this.addresses = addresses; }
    
}