package com.dentallab.representationmodel;

import org.springframework.hateoas.RepresentationModel;
import java.util.List;

public class DentistRepresentation extends RepresentationModel<DentistRepresentation> {

    private Long id;
    private String name;
    private String clinicName;

    // Contact info as lists of representation models
    private List<DentistPhoneRepresentation> phones;
    private List<DentistEmailRepresentation> emails;
    private List<DentistAddressRepresentation> addresses;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public List<DentistPhoneRepresentation> getPhones() { return phones; }
    public void setPhones(List<DentistPhoneRepresentation> phones) { this.phones = phones; }

    public List<DentistEmailRepresentation> getEmails() { return emails; }
    public void setEmails(List<DentistEmailRepresentation> emails) { this.emails = emails; }

    public List<DentistAddressRepresentation> getAddresses() { return addresses; }
    public void setAddresses(List<DentistAddressRepresentation> addresses) { this.addresses = addresses; }
}
