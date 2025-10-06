package com.dentallab.model;

public abstract class AbstractRoleProfile {
	
    private Long id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String secondLastName;
    private String primaryPhone;
    private String primaryEmail;
    private String PrimaryAddress;

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
    
}