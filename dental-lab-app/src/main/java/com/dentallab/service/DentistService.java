package com.dentallab.service;

import com.dentallab.model.Dentist;
import java.util.List;

public interface DentistService {
    Dentist getDentistById(Long id);
    List<Dentist> getAllDentists();
    Dentist createDentist(Dentist dentist);
    Dentist updateDentist(Long id, Dentist dentist);
    void deleteDentist(Long id);
}

