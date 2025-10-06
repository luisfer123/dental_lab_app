package com.dentallab.service.impl;

import com.dentallab.model.*;
import com.dentallab.persistence.entity.*;
import com.dentallab.persistence.repository.*;
import com.dentallab.service.DentistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistServiceImpl implements DentistService {

    private final DentistRepository dentistRepository;
    private final DentistPhoneRepository dentistPhoneRepository;
    private final DentistEmailRepository dentistEmailRepository;
    private final DentistAddressRepository dentistAddressRepository;

    public DentistServiceImpl(DentistRepository dentistRepository,
                              DentistPhoneRepository dentistPhoneRepository,
                              DentistEmailRepository dentistEmailRepository,
                              DentistAddressRepository dentistAddressRepository) {
        this.dentistRepository = dentistRepository;
        this.dentistPhoneRepository = dentistPhoneRepository;
        this.dentistEmailRepository = dentistEmailRepository;
        this.dentistAddressRepository = dentistAddressRepository;
    }

    @Override
    public Dentist getDentistById(Long id) {
        DentistEntity entity = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentist not found with id " + id));

        Dentist model = mapToModel(entity);

        // populate contact lists
        model.setPhones(
                dentistPhoneRepository.findByDentistId(id).stream()
                        .map(this::toPhoneModel)
                        .collect(Collectors.toList())
        );
        model.setEmails(
                dentistEmailRepository.findByDentistId(id).stream()
                        .map(this::toEmailModel)
                        .collect(Collectors.toList())
        );
        model.setAddresses(
                dentistAddressRepository.findByDentistId(id).stream()
                        .map(this::toAddressModel)
                        .collect(Collectors.toList())
        );

        return model;
    }

    @Override
    public List<Dentist> getAllDentists() {
        // You can either return dentists without contacts for list views,
        // or fetch + populate contacts for each (slower). Here’s a lean version:
        return dentistRepository.findAll().stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Dentist createDentist(Dentist dentist) {
        DentistEntity saved = dentistRepository.save(mapToEntity(dentist));
        return mapToModel(saved);
    }

    @Override
    public Dentist updateDentist(Long id, Dentist dentist) {
        DentistEntity existing = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentist not found with id " + id));
        existing.setName(dentist.getName());
        existing.setClinicName(dentist.getClinicName());
        return mapToModel(dentistRepository.save(existing));
    }

    @Override
    public void deleteDentist(Long id) {
        if (!dentistRepository.existsById(id)) {
            throw new RuntimeException("Dentist not found with id " + id);
        }
        dentistRepository.deleteById(id);
    }

    // -------- mapping helpers --------

    private Dentist mapToModel(DentistEntity entity) {
        Dentist m = new Dentist();
        m.setId(entity.getId());
        m.setName(entity.getName());
        m.setClinicName(entity.getClinicName());
        // contact lists filled in getDentistById
        return m;
    }

    private DentistEntity mapToEntity(Dentist model) {
        DentistEntity e = new DentistEntity();
        e.setId(model.getId());
        e.setName(model.getName());
        e.setClinicName(model.getClinicName());
        return e;
    }

    private DentistPhone toPhoneModel(DentistPhoneEntity e) {
        DentistPhone m = new DentistPhone();
        m.setId(e.getId());
        m.setDentistId(e.getDentist().getId());
        m.setPhone(e.getPhone());
        m.setType(e.getType());
        m.setPrimary(e.isPrimary());
        m.setActive(e.isActive());
        return m;
    }

    private DentistEmail toEmailModel(DentistEmailEntity e) {
        DentistEmail m = new DentistEmail();
        m.setId(e.getId());
        m.setDentistId(e.getDentist().getId());
        m.setEmail(e.getEmail());
        m.setType(e.getType());
        m.setPrimary(e.isPrimary());
        m.setActive(e.isActive());
        return m;
    }

    private DentistAddress toAddressModel(DentistAddressEntity e) {
        DentistAddress m = new DentistAddress();
        m.setId(e.getId());
        m.setDentistId(e.getDentist().getId());
        m.setAddress(e.getAddress());
        m.setType(e.getType());
        m.setPrimary(e.isPrimary());
        m.setActive(e.isActive());
        return m;
    }
}
