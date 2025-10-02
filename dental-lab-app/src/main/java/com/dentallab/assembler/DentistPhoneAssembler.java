package com.dentallab.assembler;

import com.dentallab.model.DentistPhone;
import com.dentallab.representationmodel.DentistPhoneRepresentation;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DentistPhoneAssembler implements RepresentationModelAssembler<DentistPhone, DentistPhoneRepresentation> {

    @Override
    public DentistPhoneRepresentation toModel(DentistPhone phone) {
        DentistPhoneRepresentation repr = new DentistPhoneRepresentation();
        repr.setId(phone.getId());
        repr.setPhone(phone.getPhone());
        repr.setType(phone.getType());
        repr.setPrimary(phone.isPrimary());
        repr.setActive(phone.isActive());

        // HATEOAS links
        repr.add(linkTo(methodOn(com.dentallab.controller.DentistController.class)
                .getDentistPhone(phone.getDentistId(), phone.getId())).withSelfRel());

        repr.add(linkTo(methodOn(com.dentallab.controller.DentistController.class)
                .getDentist(phone.getDentistId())).withRel("dentist"));

        return repr;
    }
}

