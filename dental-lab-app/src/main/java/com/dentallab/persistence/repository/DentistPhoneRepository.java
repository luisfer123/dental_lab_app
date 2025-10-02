package com.dentallab.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dentallab.persistence.entity.DentistPhoneEntity;

@Repository
public interface DentistPhoneRepository extends JpaRepository<DentistPhoneEntity, Long> {
    List<DentistPhoneEntity> findByDentistId(Long dentistId);
    List<DentistPhoneEntity> findByDentistIdAndActiveTrue(Long dentistId);
    Optional<DentistPhoneEntity> findByDentistIdAndPrimaryTrue(Long dentistId);
}
