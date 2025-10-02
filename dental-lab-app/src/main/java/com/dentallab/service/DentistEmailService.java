package com.dentallab.service;

import com.dentallab.model.DentistEmail;
import java.util.List;

public interface DentistEmailService {
    DentistEmail getEmailById(Long dentistId, Long emailId);
    List<DentistEmail> getEmailsByDentist(Long dentistId);
    DentistEmail addEmail(Long dentistId, DentistEmail email);
    DentistEmail updateEmail(Long dentistId, Long emailId, DentistEmail email);
    void deleteEmail(Long dentistId, Long emailId);
}
