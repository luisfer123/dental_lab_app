package com.dentallab.service.impl;

import com.dentallab.model.DentistPhone;
import com.dentallab.persistence.entity.DentistPhoneEntity;
import com.dentallab.persistence.repository.DentistPhoneRepository;
import com.dentallab.service.DentistPhoneService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistPhoneServiceImpl implements DentistPhoneService {

    private final DentistPhoneRepository phoneRepository;

    public DentistPhoneServiceImpl(DentistPhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public DentistPhone getPhoneById(Long dentistId, Long phoneId) {
        DentistPhoneEntity entity = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Phone not found"));
        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Phone does not belong to this dentist");
        }
        return mapToModel(entity);
    }

    @Override
    public List<DentistPhone> getPhonesByDentist(Long dentistId) {
        return phoneRepository.findByDentistId(dentistId).stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public DentistPhone addPhone(Long dentistId, DentistPhone phone) {
        DentistPhoneEntity entity = mapToEntity(phone, dentistId);

        // enforce only one primary
        if (phone.isPrimary()) {
            phoneRepository.findByDentistIdAndPrimaryTrue(dentistId)
                    .ifPresent(existing -> {
                        existing.setPrimary(false);
                        phoneRepository.save(existing);
                    });
        }

        return mapToModel(phoneRepository.save(entity));
    }

    @Override
    public DentistPhone updatePhone(Long dentistId, Long phoneId, DentistPhone phone) {
        DentistPhoneEntity entity = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Phone not found"));

        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Phone does not belong to this dentist");
        }

        entity.setPhone(phone.getPhone());
        entity.setType(phone.getType());
        entity.setPrimary(phone.isPrimary());
        entity.setActive(phone.isActive());

        // enforce only one primary
        if (phone.isPrimary()) {
            phoneRepository.findByDentistIdAndPrimaryTrue(dentistId)
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(phoneId)) {
                            existing.setPrimary(false);
                            phoneRepository.save(existing);
                        }
                    });
        }

        return mapToModel(phoneRepository.save(entity));
    }

    @Override
    public void deletePhone(Long dentistId, Long phoneId) {
        DentistPhoneEntity entity = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Phone not found"));
        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Phone does not belong to this dentist");
        }
        phoneRepository.delete(entity);
    }

    // ================= PRIVATE HELPERS =================

    private DentistPhone mapToModel(DentistPhoneEntity entity) {
        DentistPhone model = new DentistPhone();
        model.setId(entity.getId());
        model.setDentistId(entity.getDentist().getId());
        model.setPhone(entity.getPhone());
        model.setType(entity.getType());
        model.setPrimary(entity.isPrimary());
        model.setActive(entity.isActive());
        return model;
    }

    private DentistPhoneEntity mapToEntity(DentistPhone model, Long dentistId) {
        DentistPhoneEntity entity = new DentistPhoneEntity();
        entity.setId(model.getId());
        entity.setPhone(model.getPhone());
        entity.setType(model.getType());
        entity.setPrimary(model.isPrimary());
        entity.setActive(model.isActive());
        // dentist will be set by JPA (fetch DentistEntity by ID if needed)
        return entity;
    }
}

