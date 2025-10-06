package com.dentallab.controller;

import com.dentallab.assembler.DentistAssembler;
import com.dentallab.assembler.DentistPhoneAssembler;
import com.dentallab.assembler.DentistEmailAssembler;
import com.dentallab.assembler.DentistAddressAssembler;
import com.dentallab.model.Dentist;
import com.dentallab.model.DentistPhone;
import com.dentallab.model.DentistEmail;
import com.dentallab.model.DentistAddress;
import com.dentallab.representationmodel.DentistRepresentation;
import com.dentallab.representationmodel.DentistPhoneRepresentation;
import com.dentallab.representationmodel.DentistEmailRepresentation;
import com.dentallab.representationmodel.DentistAddressRepresentation;
import com.dentallab.service.DentistService;
import com.dentallab.service.DentistPhoneService;
import com.dentallab.service.DentistEmailService;
import com.dentallab.service.DentistAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dentists")
public class DentistController {

    private final DentistService dentistService;
    private final DentistPhoneService phoneService;
    private final DentistEmailService emailService;
    private final DentistAddressService addressService;

    private final DentistAssembler dentistAssembler;
    private final DentistPhoneAssembler phoneAssembler;
    private final DentistEmailAssembler emailAssembler;
    private final DentistAddressAssembler addressAssembler;

    public DentistController(DentistService dentistService,
                             DentistPhoneService phoneService,
                             DentistEmailService emailService,
                             DentistAddressService addressService,
                             DentistAssembler dentistAssembler,
                             DentistPhoneAssembler phoneAssembler,
                             DentistEmailAssembler emailAssembler,
                             DentistAddressAssembler addressAssembler) {
        this.dentistService = dentistService;
        this.phoneService = phoneService;
        this.emailService = emailService;
        this.addressService = addressService;
        this.dentistAssembler = dentistAssembler;
        this.phoneAssembler = phoneAssembler;
        this.emailAssembler = emailAssembler;
        this.addressAssembler = addressAssembler;
    }

    // ---------------- Dentist ---------------- //

    @GetMapping("/{id}")
    public ResponseEntity<DentistRepresentation> getDentist(@PathVariable Long id) {
        Dentist dentist = dentistService.getDentistById(id);
        return ResponseEntity.ok(dentistAssembler.toModel(dentist));
    }

    @GetMapping
    public ResponseEntity<List<DentistRepresentation>> getAllDentists() {
        List<Dentist> dentists = dentistService.getAllDentists();
        return ResponseEntity.ok(
            dentists.stream().map(dentistAssembler::toModel).collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<DentistRepresentation> createDentist(@RequestBody Dentist dentist) {
        return ResponseEntity.ok(dentistAssembler.toModel(dentistService.createDentist(dentist)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DentistRepresentation> updateDentist(
            @PathVariable Long id,
            @RequestBody Dentist dentist) {
        return ResponseEntity.ok(dentistAssembler.toModel(dentistService.updateDentist(id, dentist)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDentist(@PathVariable Long id) {
        dentistService.deleteDentist(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Phones ---------------- //

    @GetMapping("/{id}/phones")
    public ResponseEntity<List<DentistPhoneRepresentation>> getPhones(@PathVariable Long id) {
        return ResponseEntity.ok(
            phoneService.getPhonesByDentist(id).stream()
                .map(phoneAssembler::toModel)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}/phones/{phoneId}")
    public ResponseEntity<DentistPhoneRepresentation> getDentistPhone(
            @PathVariable Long id,
            @PathVariable Long phoneId) {
        return ResponseEntity.ok(
            phoneAssembler.toModel(phoneService.getPhoneById(id, phoneId))
        );
    }

    @PostMapping("/{id}/phones")
    public ResponseEntity<DentistPhoneRepresentation> addPhone(
            @PathVariable Long id,
            @RequestBody DentistPhone phone) {
        return ResponseEntity.ok(
            phoneAssembler.toModel(phoneService.addPhone(id, phone))
        );
    }

    @PutMapping("/{id}/phones/{phoneId}")
    public ResponseEntity<DentistPhoneRepresentation> updatePhone(
            @PathVariable Long id,
            @PathVariable Long phoneId,
            @RequestBody DentistPhone phone) {
        return ResponseEntity.ok(
            phoneAssembler.toModel(phoneService.updatePhone(id, phoneId, phone))
        );
    }

    @DeleteMapping("/{id}/phones/{phoneId}")
    public ResponseEntity<Void> deletePhone(
            @PathVariable Long id,
            @PathVariable Long phoneId) {
        phoneService.deletePhone(id, phoneId);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Emails ---------------- //

    @GetMapping("/{id}/emails")
    public ResponseEntity<List<DentistEmailRepresentation>> getEmails(@PathVariable Long id) {
        return ResponseEntity.ok(
            emailService.getEmailsByDentist(id).stream()
                .map(emailAssembler::toModel)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}/emails/{emailId}")
    public ResponseEntity<DentistEmailRepresentation> getDentistEmail(
            @PathVariable Long id,
            @PathVariable Long emailId) {
        return ResponseEntity.ok(
            emailAssembler.toModel(emailService.getEmailById(id, emailId))
        );
    }

    @PostMapping("/{id}/emails")
    public ResponseEntity<DentistEmailRepresentation> addEmail(
            @PathVariable Long id,
            @RequestBody DentistEmail email) {
        return ResponseEntity.ok(
            emailAssembler.toModel(emailService.addEmail(id, email))
        );
    }

    @PutMapping("/{id}/emails/{emailId}")
    public ResponseEntity<DentistEmailRepresentation> updateEmail(
            @PathVariable Long id,
            @PathVariable Long emailId,
            @RequestBody DentistEmail email) {
        return ResponseEntity.ok(
            emailAssembler.toModel(emailService.updateEmail(id, emailId, email))
        );
    }

    @DeleteMapping("/{id}/emails/{emailId}")
    public ResponseEntity<Void> deleteEmail(
            @PathVariable Long id,
            @PathVariable Long emailId) {
        emailService.deleteEmail(id, emailId);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Addresses ---------------- //

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<DentistAddressRepresentation>> getAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(
            addressService.getAddressesByDentist(id).stream()
                .map(addressAssembler::toModel)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<DentistAddressRepresentation> getDentistAddress(
            @PathVariable Long id,
            @PathVariable Long addressId) {
        return ResponseEntity.ok(
            addressAssembler.toModel(addressService.getAddressById(id, addressId))
        );
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<DentistAddressRepresentation> addAddress(
            @PathVariable Long id,
            @RequestBody DentistAddress address) {
        return ResponseEntity.ok(
            addressAssembler.toModel(addressService.addAddress(id, address))
        );
    }

    @PutMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<DentistAddressRepresentation> updateAddress(
            @PathVariable Long id,
            @PathVariable Long addressId,
            @RequestBody DentistAddress address) {
        return ResponseEntity.ok(
            addressAssembler.toModel(addressService.updateAddress(id, addressId, address))
        );
    }

    @DeleteMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long id,
            @PathVariable Long addressId) {
        addressService.deleteAddress(id, addressId);
        return ResponseEntity.noContent().build();
    }
}

