package com.dentallab.persistence.repository;

import com.dentallab.persistence.entity.DentistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DentistRepository extends JpaRepository<DentistEntity, Long> {
    // You can add custom queries if needed, e.g.:
    // List<DentistEntity> findByClinicName(String clinicName);
}

