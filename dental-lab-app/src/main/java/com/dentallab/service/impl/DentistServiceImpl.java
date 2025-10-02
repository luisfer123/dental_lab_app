package com.dentallab.service.impl;

import com.dentallab.model.Dentist;
import com.dentallab.persistence.entity.DentistEntity;
import com.dentallab.persistence.repository.DentistRepository;
import com.dentallab.service.DentistService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistServiceImpl implements DentistService {

    private final DentistRepository dentistRepository;

    public DentistServiceImpl(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    @Override
    public Dentist getDentistById(Long id) {
        return dentistRepository.findById(id)
                .map(this::mapToModel)
                .orElseThrow(() -> new RuntimeException("Dentist not found"));
    }

    @Override
    public List<Dentist> getAllDentists() {
        return dentistRepository.findAll().stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Dentist createDentist(Dentist dentist) {
        DentistEntity entity = mapToEntity(dentist);
        return mapToModel(dentistRepository.save(entity));
    }

    @Override
    public Dentist updateDentist(Long id, Dentist dentist) {
        DentistEntity existing = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentist not found"));
        existing.setName(dentist.getName());
        existing.setClinicName(dentist.getClinicName());
        return mapToModel(dentistRepository.save(existing));
    }

    @Override
    public void deleteDentist(Long id) {
        dentistRepository.deleteById(id);
    }

    // === Mapping helpers ===
    private Dentist mapToModel(DentistEntity entity) {
        Dentist model = new Dentist();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setClinicName(entity.getClinicName());
        // contacts can be mapped later with separate services
        return model;
    }

    private DentistEntity mapToEntity(Dentist model) {
        DentistEntity entity = new DentistEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setClinicName(model.getClinicName());
        return entity;
    }
}

