package com.dentallab.persistence.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Dentist")
public class DentistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dentist_id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "second_last_name")
    private String secondLastName;

    @Column(name = "clinic_name")
    private String clinicName;

    // Relationships --------------------------

//    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PatientEntity> patients;
//
//    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CaseOrderEntity> caseOrders;
//
//    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<InvoiceEntity> invoices;
//
//    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PaymentEntity> payments;

    // Getters and setters ---------------------
    // (Omit Lombok for clarity, but you can use @Data or @Getter/@Setter if you prefer)
    
    // Dentist-specific pricing
//    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<WorkPriceEntity> workPrices;
//    
    // Contacts
    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DentistPhoneEntity> phones;

    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DentistEmailEntity> emails;

    @OneToMany(mappedBy = "dentist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DentistAddressEntity> addresses;

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

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }
    
}