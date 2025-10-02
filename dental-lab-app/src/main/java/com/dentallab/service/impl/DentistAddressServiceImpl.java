package com.dentallab.service.impl;

import com.dentallab.model.DentistAddress;
import com.dentallab.persistence.entity.DentistAddressEntity;
import com.dentallab.persistence.repository.DentistAddressRepository;
import com.dentallab.service.DentistAddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DentistAddressServiceImpl implements DentistAddressService {

    private final DentistAddressRepository addressRepository;

    public DentistAddressServiceImpl(DentistAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public DentistAddress getAddressById(Long dentistId, Long addressId) {
        DentistAddressEntity entity = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Address does not belong to this dentist");
        }
        return mapToModel(entity);
    }

    @Override
    public List<DentistAddress> getAddressesByDentist(Long dentistId) {
        return addressRepository.findByDentistId(dentistId).stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public DentistAddress addAddress(Long dentistId, DentistAddress address) {
        DentistAddressEntity entity = mapToEntity(address, dentistId);

        // enforce only one primary address
        if (address.isPrimary()) {
            addressRepository.findByDentistIdAndPrimaryTrue(dentistId)
                    .ifPresent(existing -> {
                        existing.setPrimary(false);
                        addressRepository.save(existing);
                    });
        }

        return mapToModel(addressRepository.save(entity));
    }

    @Override
    public DentistAddress updateAddress(Long dentistId, Long addressId, DentistAddress address) {
        DentistAddressEntity entity = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Address does not belong to this dentist");
        }

        entity.setAddress(address.getAddress());
        entity.setType(address.getType());
        entity.setPrimary(address.isPrimary());
        entity.setActive(address.isActive());

        // enforce only one primary
        if (address.isPrimary()) {
            addressRepository.findByDentistIdAndPrimaryTrue(dentistId)
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(addressId)) {
                            existing.setPrimary(false);
                            addressRepository.save(existing);
                        }
                    });
        }

        return mapToModel(addressRepository.save(entity));
    }

    @Override
    public void deleteAddress(Long dentistId, Long addressId) {
        DentistAddressEntity entity = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        if (!entity.getDentist().getId().equals(dentistId)) {
            throw new RuntimeException("Address does not belong to this dentist");
        }
        addressRepository.delete(entity);
    }

    // ================= PRIVATE HELPERS =================

    private DentistAddress mapToModel(DentistAddressEntity entity) {
        DentistAddress model = new DentistAddress();
        model.setId(entity.getId());
        model.setDentistId(entity.getDentist().getId());
        model.setAddress(entity.getAddress());
        model.setType(entity.getType());
        model.setPrimary(entity.isPrimary());
        model.setActive(entity.isActive());
        return model;
    }

    private DentistAddressEntity mapToEntity(DentistAddress model, Long dentistId) {
        DentistAddressEntity entity = new DentistAddressEntity();
        entity.setId(model.getId());
        entity.setAddress(model.getAddress());
        entity.setType(model.getType());
        entity.setPrimary(model.isPrimary());
        entity.setActive(model.isActive());
        // dentist will be set later when linking
        return entity;
    }
}

