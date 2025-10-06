package com.dentallab.assembler;

import com.dentallab.model.DentistAddress;
import com.dentallab.representationmodel.DentistAddressRepresentation;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DentistAddressAssembler implements RepresentationModelAssembler<DentistAddress, DentistAddressRepresentation> {

    @Override
    public DentistAddressRepresentation toModel(DentistAddress address) {
        DentistAddressRepresentation repr = new DentistAddressRepresentation();
        repr.setId(address.getId());
        repr.setAddress(address.getAddress());
        repr.setType(address.getType());
        repr.setPrimary(address.isPrimary());
        repr.setActive(address.isActive());

        // HATEOAS links
        repr.add(linkTo(methodOn(com.dentallab.controller.DentistController.class)
                .getDentistAddress(address.getDentistId(), address.getId())).withSelfRel());

        repr.add(linkTo(methodOn(com.dentallab.controller.DentistController.class)
                .getDentist(address.getDentistId())).withRel("dentist"));

        return repr;
    }
}

