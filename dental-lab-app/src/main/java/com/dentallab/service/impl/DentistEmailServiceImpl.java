package com.dentallab.service.impl;

import com.dentallab.model.DentistEmail;
import com.dentallab.persistence.entity.DentistEmailEntity;
import com.dentallab.persistence.repository.DentistEmailRepository;
import com.dentallab.service.DentistEmailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistEmailServiceImpl implements DentistEmailService {

    private final DentistEmailRepository emailRepository;

    public DentistEmailServiceImpl(DentistEmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public DentistEmail getEmailById(Long dentistId, Long emailId) {
        DentistEmailEntity entity = emailRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found"));
        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Email does not belong to this dentist");
        }
        return mapToModel(entity);
    }

    @Override
    public List<DentistEmail> getEmailsByDentist(Long dentistId) {
        return emailRepository.findByDentistId(dentistId).stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public DentistEmail addEmail(Long dentistId, DentistEmail email) {
        DentistEmailEntity entity = mapToEntity(email, dentistId);

        // enforce only one primary email
        if (email.isPrimary()) {
            emailRepository.findByDentistIdAndPrimaryTrue(dentistId)
                    .ifPresent(existing -> {
                        existing.setPrimary(false);
                        emailRepository.save(existing);
                    });
        }

        return mapToModel(emailRepository.save(entity));
    }

    @Override
    public DentistEmail updateEmail(Long dentistId, Long emailId, DentistEmail email) {
        DentistEmailEntity entity = emailRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Email does not belong to this dentist");
        }

        entity.setEmail(email.getEmail());
        entity.setType(email.getType());
        entity.setPrimary(email.isPrimary());
        entity.setActive(email.isActive());

        // enforce only one primary
        if (email.isPrimary()) {
            emailRepository.findByDentistIdAndPrimaryTrue(dentistId)
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(emailId)) {
                            existing.setPrimary(false);
                            emailRepository.save(existing);
                        }
                    });
        }

        return mapToModel(emailRepository.save(entity));
    }

    @Override
    public void deleteEmail(Long dentistId, Long emailId) {
        DentistEmailEntity entity = emailRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found"));
        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Email does not belong to this dentist");
        }
        emailRepository.delete(entity);
    }

    // ================= PRIVATE HELPERS =================

    private DentistEmail mapToModel(DentistEmailEntity entity) {
        DentistEmail model = new DentistEmail();
        model.setId(entity.getId());
        model.setDentistId(entity.getDentist().getId());
        model.setEmail(entity.getEmail());
        model.setType(entity.getType());
        model.setPrimary(entity.isPrimary());
        model.setActive(entity.isActive());
        return model;
    }

    private DentistEmailEntity mapToEntity(DentistEmail model, Long dentistId) {
        DentistEmailEntity entity = new DentistEmailEntity();
        entity.setId(model.getId());
        entity.setEmail(model.getEmail());
        entity.setType(model.getType());
        entity.setPrimary(model.isPrimary());
        entity.setActive(model.isActive());
        // dentist will be set by JPA or service logic when linking
        return entity;
    }
}

