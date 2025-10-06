package com.dentallab.assembler;

import com.dentallab.model.Dentist;
import com.dentallab.representationmodel.DentistRepresentation;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DentistAssembler implements RepresentationModelAssembler<Dentist, DentistRepresentation> {

    private final DentistPhoneAssembler phoneAssembler;
    private final DentistEmailAssembler emailAssembler;
    private final DentistAddressAssembler addressAssembler;

    public DentistAssembler(DentistPhoneAssembler phoneAssembler,
                            DentistEmailAssembler emailAssembler,
                            DentistAddressAssembler addressAssembler) {
        this.phoneAssembler = phoneAssembler;
        this.emailAssembler = emailAssembler;
        this.addressAssembler = addressAssembler;
    }

    @Override
    public DentistRepresentation toModel(Dentist dentist) {
        DentistRepresentation repr = new DentistRepresentation();
        repr.setId(dentist.getId());
        repr.setName(dentist.getName());
        repr.setClinicName(dentist.getClinicName());

        if (dentist.getPhones() != null) {
            repr.setPhones(
                dentist.getPhones().stream()
                        .map(phoneAssembler::toModel)
                        .collect(Collectors.toList())
            );
        }

        if (dentist.getEmails() != null) {
            repr.setEmails(
                dentist.getEmails().stream()
                        .map(emailAssembler::toModel)
                        .collect(Collectors.toList())
            );
        }

        if (dentist.getAddresses() != null) {
            repr.setAddresses(
                dentist.getAddresses().stream()
                        .map(addressAssembler::toModel)
                        .collect(Collectors.toList())
            );
        }

        // Self link
        repr.add(linkTo(methodOn(com.dentallab.controller.DentistController.class)
                .getDentist(dentist.getId())).withSelfRel());

        return repr;
    }
}
