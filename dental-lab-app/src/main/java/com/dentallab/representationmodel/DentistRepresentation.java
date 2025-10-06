package com.dentallab.representationmodel;

import org.springframework.hateoas.RepresentationModel;
import java.util.List;

public class DentistRepresentation extends RepresentationModel<DentistRepresentation> {

	private String clinicName;
    private String licenseNumber;
    private String specialty;

    // Contact info as lists of representation models
    private List<DentistPhoneRepresentation> phones;
    private List<DentistEmailRepresentation> emails;
    private List<DentistAddressRepresentation> addresses;

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public List<DentistPhoneRepresentation> getPhones() { return phones; }
    public void setPhones(List<DentistPhoneRepresentation> phones) { this.phones = phones; }

    public List<DentistEmailRepresentation> getEmails() { return emails; }
    public void setEmails(List<DentistEmailRepresentation> emails) { this.emails = emails; }

    public List<DentistAddressRepresentation> getAddresses() { return addresses; }
    public void setAddresses(List<DentistAddressRepresentation> addresses) { this.addresses = addresses; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}