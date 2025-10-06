package com.dentallab.model;

public abstract class AbstractRoleProfile {
	
    private Long id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String secondLastName;
    private String phoneNumber;
    private String address;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSecondName() { return secondName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSecondLastName() { return secondLastName; }
    public void setSecondLastName(String secondLastName) { this.secondLastName = secondLastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}