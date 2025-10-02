package com.dentallab.service;

import com.dentallab.model.DentistAddress;
import java.util.List;

public interface DentistAddressService {
    DentistAddress getAddressById(Long dentistId, Long addressId);
    List<DentistAddress> getAddressesByDentist(Long dentistId);
    DentistAddress addAddress(Long dentistId, DentistAddress address);
    DentistAddress updateAddress(Long dentistId, Long addressId, DentistAddress address);
    void deleteAddress(Long dentistId, Long addressId);
}

