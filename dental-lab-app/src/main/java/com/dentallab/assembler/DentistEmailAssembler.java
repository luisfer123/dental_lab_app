package com.dentallab.assembler;

import com.dentallab.model.DentistEmail;
import com.dentallab.representationmodel.DentistEmailRepresentation;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DentistEmailAssembler implements RepresentationModelAssembler<DentistEmail, DentistEmailRepresentation> {

    @Override
    public DentistEmailRepresentation toModel(DentistEmail email) {
        DentistEmailRepresentation repr = new DentistEmailRepresentation();
        repr.setId(email.getId());
        repr.setEmail(email.getEmail());
        repr.setType(email.getType());
        repr.setPrimary(email.isPrimary());
        repr.setActive(email.isActive());

        // HATEOAS links
        repr.add(linkTo(methodOn(com.dentallab.controller.DentistController.class)
                .getDentistEmail(email.getDentistId(), email.getId())).withSelfRel());

        repr.add(linkTo(methodOn(com.dentallab.controller.DentistController.class)
                .getDentist(email.getDentistId())).withRel("dentist"));

        return repr;
    }
}
