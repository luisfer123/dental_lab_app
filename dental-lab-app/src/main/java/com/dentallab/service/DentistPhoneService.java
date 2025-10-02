package com.dentallab.service;

import com.dentallab.model.DentistPhone;
import java.util.List;

public interface DentistPhoneService {
    DentistPhone getPhoneById(Long dentistId, Long phoneId);
    List<DentistPhone> getPhonesByDentist(Long dentistId);
    DentistPhone addPhone(Long dentistId, DentistPhone phone);
    DentistPhone updatePhone(Long dentistId, Long phoneId, DentistPhone phone);
    void deletePhone(Long dentistId, Long phoneId);
}

