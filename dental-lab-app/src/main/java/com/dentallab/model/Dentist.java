package com.dentallab.model;

import java.util.List;

public class Dentist {

    private Long id;
    private String name;
    private String clinicName;
    private List<String> phones;
    private List<String> emails;
    private List<String> addresses;

    // Constructors --------------------
    public Dentist() {}

    public Dentist(Long id, String name, String clinicName, String phone, String email, String address) {
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

    public List<String> getPhones() { return phones; }
    public void setPhones(List<String> phones) { this.phones = phones; }

    public List<String> getEmails() { return emails; }
    public void setEmails(List<String> emails) { this.emails = emails; }

    public List<String> getAddresses() { return addresses; }
    public void setAddresses(List<String> addresses) { this.addresses = addresses; }
}