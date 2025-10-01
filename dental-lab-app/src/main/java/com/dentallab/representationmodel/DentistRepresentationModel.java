package com.dentallab.representationmodel;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DentistRepresentationModel extends RepresentationModel<DentistRepresentationModel> {

    private Long id;
    private String name;
    private String clinicName;
    private List<String> phones;
    private List<String> emails;
    private List<String> addresses;

    // Getters and setters
    
    public List<String> getPhones() { return phones; }
	public void setPhones(List<String> phones) { this.phones = phones;}
	
	public List<String> getEmails() { return emails; }
	public void setEmails(List<String> emails) { this.emails = emails; }
	
	public List<String> getAddresses() { return addresses; }
	public void setAddresses(List<String> addresses) { this.addresses = addresses; }
	
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }
    
}
